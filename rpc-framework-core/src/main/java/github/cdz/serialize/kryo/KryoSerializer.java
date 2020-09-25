package github.cdz.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import github.cdz.dto.RpcRequest;
import github.cdz.dto.RpcResponse;
import github.cdz.exception.SerializeException;
import github.cdz.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * KyroSerializer
 *
 * @author chendezhi
 * @date 2020/9/24 14:13
 * @since 1.0.0
 */
@Slf4j
public class KryoSerializer implements Serializer {

    private final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(()->{
        Kryo kryo = new Kryo();
        kryo.register(RpcRequest.class);
        kryo.register(RpcResponse.class);
        return kryo;
    });

    @Override
    public byte[] serialize(Object o) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output outputStream = new Output(byteArrayOutputStream);){
            Kryo kryo = KRYO_THREAD_LOCAL.get();
            kryo.writeObject(outputStream,o);
            return outputStream.toBytes();
        } catch (Exception e) {
            log.error("序列化失败：",e);
            throw new SerializeException("序列化失败");
        }finally {
            KRYO_THREAD_LOCAL.remove();
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = KRYO_THREAD_LOCAL.get();
            Object o = kryo.readObject(input, clazz);
            //clazz.cast(o) 就是强转一下 将 object转为 对应的class类型
            return clazz.cast(o);
        } catch (Exception e) {
            log.error("序列化失败：",e);
            throw new SerializeException("序列化失败");
        }finally {
            KRYO_THREAD_LOCAL.remove();
        }
    }
}
