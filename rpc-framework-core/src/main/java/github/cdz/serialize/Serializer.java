package github.cdz.serialize;

/**
 * Serializer
 *
 * @author chendezhi
 * @date 2020/9/24 13:44
 * @since 1.0.0
 */
public interface Serializer {

    /**
     * 序列化 obj -> byte[]数组
     * @param o
     * @return
     */
    byte[] serialize(Object o);

    /**
     * 反序列化
     * @param bytes 序列化后的byte数组
     * @param clazz 序列化类型
     * @param <T> 返回对象
     * @return
     */
    <T> T deserialize(byte[] bytes,Class<T> clazz);
}
