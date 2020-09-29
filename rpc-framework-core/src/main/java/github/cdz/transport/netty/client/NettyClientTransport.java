package github.cdz.transport.netty.client;

import github.cdz.dto.RpcRequest;
import github.cdz.dto.RpcResponse;
import github.cdz.registry.ServiceDiscovery;
import github.cdz.registry.zk.ZkServiceDiscovery;
import github.cdz.transport.ClientTransport;
import github.cdz.utils.checker.RpcMessageChecker;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

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

    private ServiceDiscovery serviceDiscovery;

    private UnprocessedRequests unprocessedRequests;

    public NettyClientTransport(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
        this.unprocessedRequests = new UnprocessedRequests();
    }

    @Override
    public CompletableFuture<RpcResponse> sendRpcRequest(RpcRequest rpcRequest) {
        //构建返回值
        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();
        try {
            Channel channel = channelProvider.get(serviceDiscovery.lookupService(rpcRequest.getInterfaceName()));
            if (channel != null) {
                unprocessedRequests.put(rpcRequest.getRequestId(),resultFuture);
                channel.writeAndFlush(rpcRequest).addListener(future -> {
                    if (future.isSuccess()) {
                        log.info("client send msg:{}", rpcRequest);
                    } else {
                        resultFuture.completeExceptionally(future.cause());
                        log.error("client send msg fail");
                    }
                });
                channel.closeFuture().sync();
            }
        } catch (Exception e) {
            unprocessedRequests.remove(rpcRequest.getRequestId());
            log.error("sendRpcRequest 异常 ", e);
        }
        return resultFuture;
    }
}
