package github.cdz.transport.netty;

import github.cdz.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * NettyKyroCode
 *
 * @author chendezhi
 * @date 2020/9/24 15:10
 * @since 1.0.0
 * <p>
 * netty传输 使用kryo 解码器
 */
@AllArgsConstructor
public class NettyKryoDecode extends ByteToMessageDecoder {

    private Serializer serializer;
    private Class<?> genericClazz;

    private static final int BODY_LEN = 4;//int四字节

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //1. 此方法返回可读长度，因为我们自定义的是 头-》body的数组长度值（int）本身必须占四字节，所以小于四字节一律不考虑
        if (in.readableBytes() >= BODY_LEN) {
            //2. 标记当前要读的位置，以便重试
            in.markReaderIndex();
            //3. 获取body长度
            int dataLen = in.readInt();
            //4. 校验不合理情况
            if (dataLen < 0 || in.readableBytes() < 0) {
                return;
            }
            if (in.readableBytes()<dataLen){
                //5. 当小于datalen说明数据不完整,重置读位置
                in.resetReaderIndex();
                return;
            }
            //6. 可以反序列化了
            byte[] bytes = new byte[dataLen];
            in.readBytes(bytes);
            Object obj = serializer.deserialize(bytes, genericClazz);
            out.add(obj);
        }
    }
}
