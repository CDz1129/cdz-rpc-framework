package github.cdz.transport.netty;

import github.cdz.RequestHandle;
import github.cdz.RpcRequestHandle;
import github.cdz.dto.RpcRequest;
import github.cdz.dto.RpcResponse;
import github.cdz.registry.DefaultServiceRegistry;
import github.cdz.registry.ServiceRegistry;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * NettyServerHandler
 *
 * @author chendezhi
 * @date 2020/9/24 16:19
 * @since 1.0.0
 */
@Slf4j
@AllArgsConstructor
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static ServiceRegistry serviceRegistry;
    private static RequestHandle requestHandle;
    {
        serviceRegistry = new DefaultServiceRegistry();
        requestHandle = new RpcRequestHandle();
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof RpcRequest) {
                RpcRequest rpcRequest = (RpcRequest) msg;
                log.info("server receive msg: {}",msg);
                String interfaceName = rpcRequest.getInterfaceName();
                Object service = serviceRegistry.getService(interfaceName);
                RpcResponse rpcResponse = requestHandle.handle(rpcRequest, service);
                log.info("server get result: {}",rpcResponse);
                //写回去
                // 这里要注意 写回去的时候，不要写回去错误对象了——rpcResponse 和使用错误方法是 writeAndFlush
                ChannelFuture channelFuture = ctx.channel().writeAndFlush(rpcResponse);
                channelFuture.addListener(ChannelFutureListener.CLOSE);
            }
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("server catch exception",cause);
        ctx.close();
    }
}
