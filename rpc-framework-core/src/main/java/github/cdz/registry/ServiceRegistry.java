package github.cdz.registry;

import github.cdz.extension.SPI;

import java.net.InetSocketAddress;

/**
 * ServiceRegistry
 *
 * @author chendezhi
 * @date 2020/9/23 10:12
 * @since 1.0.0
 * 注册接口——未来可以使用zk注册
 */
@SPI
public interface ServiceRegistry {
    void registryService(String serviceName, InetSocketAddress inetSocketAddress);
}
