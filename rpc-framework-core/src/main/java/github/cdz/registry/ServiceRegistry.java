package github.cdz.registry;

/**
 * ServiceRegistry
 *
 * @author chendezhi
 * @date 2020/9/23 10:12
 * @since 1.0.0
 * 注册接口——未来可以使用zk注册
 */
public interface ServiceRegistry {

    <T> void registry(T service);

    Object getService(String serviceName);

}
