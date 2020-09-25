package github.cdz.transport.netty.client;

import github.cdz.dto.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * NettyClientHandler
 *
 * @author chendezhi
 * @date 2020/9/24 15:33
 * @since 1.0.0
 */
@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof RpcResponse) {
                RpcResponse rpcResponse = (RpcResponse) msg;
                log.info("client receive msg:{}",msg);
                //将结果存入
                AttributeKey<RpcResponse> attributeKey = AttributeKey.valueOf("rpcResponse");
                ctx.channel().attr(attributeKey).set(rpcResponse);
                ctx.channel().close();
            }
        }finally {
            //msg不止有RpcResponse 还有其他的netty内部变量
            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("server catch exception",cause);
        ctx.close();
    }
}
