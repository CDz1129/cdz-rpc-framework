package github.cdz.registry;

import github.cdz.extension.SPI;

import java.net.InetSocketAddress;

/**
 * ServiceDiscovery
 *
 * @author chendezhi
 * @date 2020/9/28 14:13
 * @since 1.0.0
 *
 * 服务发现接口
 */
@SPI
public interface ServiceDiscovery {
    InetSocketAddress lookupService(String serviceName);
}
