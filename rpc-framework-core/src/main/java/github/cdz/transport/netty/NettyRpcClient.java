package github.cdz.transport.netty;

import github.cdz.RpcClient;
import github.cdz.dto.RpcRequest;
import github.cdz.dto.RpcResponse;
import github.cdz.enums.RpcErrorMessageEnum;
import github.cdz.enums.RpcResponseCode;
import github.cdz.exception.RpcException;
import github.cdz.serialize.kryo.KryoSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * NettyRpcClient
 *
 * @author chendezhi
 * @date 2020/9/24 16:31
 * @since 1.0.0
 */
@Slf4j
public class NettyRpcClient implements RpcClient {

    private String host;
    private int port;

    public NettyRpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private static final Bootstrap b;

    static {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        b = new Bootstrap();
        KryoSerializer kryoSerializer = new KryoSerializer();
        b.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast(new NettyKryoDecode(kryoSerializer, RpcResponse.class))
                                .addLast(new NettyKryoEncode(kryoSerializer, RpcRequest.class))
                                .addLast(new NettyClientHandler());
                    }
                });
    }


    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {

        try {
            ChannelFuture f = b.connect(host, port).sync();
            Channel channel = f.channel();
            if (channel != null) {
                channel.writeAndFlush(rpcRequest).addListener(future -> {
                    if (future.isSuccess()) {
                        log.info("client send msg:{}", rpcRequest);
                    } else {
                        log.error("client send msg fail");
                    }
                });
                channel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                RpcResponse rpcResponse = channel.attr(key).get();
                return rpcResponse.getData();
            }
        } catch (Exception e) {
            log.error("sendRpcRequest 异常 ", e);
        }
        return null;
    }
}
