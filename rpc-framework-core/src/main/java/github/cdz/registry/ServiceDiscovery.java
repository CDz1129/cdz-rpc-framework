package github.cdz.registry;

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
public interface ServiceDiscovery {
    InetSocketAddress lookupService(String serviceName);
}
