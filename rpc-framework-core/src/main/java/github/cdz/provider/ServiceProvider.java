package github.cdz.provider;

/**
 * ServiceProvider
 *
 * @author chendezhi
 * @date 2020/9/28 15:54
 * @since 1.0.0
 *
 * 服务提供者
 */
public interface ServiceProvider {

    <T>void pushService(T service);

    Object getService(String serviceName);
}
