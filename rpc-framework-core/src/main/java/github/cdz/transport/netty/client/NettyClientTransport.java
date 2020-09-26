package github.cdz.transport.netty.client;

import github.cdz.dto.RpcRequest;
import github.cdz.dto.RpcResponse;
import github.cdz.transport.ClientTransport;
import github.cdz.utils.checker.RpcMessageChecker;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * NettyClientTransport
 *
 * @author chendezhi
 * @date 2020/9/26 10:57
 * @since 1.0.0
 */
@Slf4j
public class NettyClientTransport implements ClientTransport {

    private static ChannelProvider channelProvider = new ChannelProvider();;

    private InetSocketAddress inetSocketAddress;

    public NettyClientTransport(InetSocketAddress inetSocketAddress) {
        this.inetSocketAddress = inetSocketAddress;
    }

    @Override
    public RpcResponse sendRpcRequest(RpcRequest rpcRequest) {
        try {
            Channel channel = channelProvider.get(inetSocketAddress);
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
                RpcMessageChecker.check(rpcRequest, rpcResponse);
                return rpcResponse;
            }
        } catch (Exception e) {
            log.error("sendRpcRequest 异常 ", e);
        }
        return null;
    }
}
