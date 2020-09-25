package github.cdz.transport.netty.codec;

import github.cdz.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

/**
 * NettyKyroCode
 *
 * @author chendezhi
 * @date 2020/9/24 15:10
 * @since 1.0.0
 *
 * netty传输 使用kryo 编码器
 */
@AllArgsConstructor
public class NettyKryoEncode extends MessageToByteEncoder<Object> {

    private Serializer serializer;
    private Class<?> genericClazz;

    /**
     * 编码器 将object编码为 ByteBuf
     *
     * 编码方式为：ByteBuf头为 body（byte[]长度）后续紧跟着byte[]内容
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (genericClazz.isInstance(msg)) {
            byte[] body = serializer.serialize(msg);
            int length = body.length;
            //1. 头写入 长度 int 4字节
            out.writeInt(length);
            //2. 后续写入 内容
            out.writeBytes(body);
        }
    }
}