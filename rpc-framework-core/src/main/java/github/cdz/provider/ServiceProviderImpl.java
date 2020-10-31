package github.cdz.provider;

import github.cdz.enums.RpcErrorMessageEnum;
import github.cdz.exception.RpcException;
import github.cdz.extension.ExtensionLoader;
import github.cdz.registry.ServiceRegistry;
import github.cdz.transport.netty.server.NettyRpcServer;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ServiceProviderImpl
 *
 * @author chendezhi
 * @date 2020/9/28 15:54
 * @since 1.0.0
 *
 * 专供server调用
 */
@Slf4j
public class ServiceProviderImpl implements ServiceProvider {

    /**
     * key: rpc service name(interface name)
     * value: service object
     */
    private static final Map<String, Object> serviceMap;
    private static final Set<String> registeredService;
    private static final ServiceRegistry serviceRegistry;

    static {
        serviceMap = new ConcurrentHashMap<>();
        registeredService = ConcurrentHashMap.newKeySet();
        serviceRegistry = ExtensionLoader.getExtensionLoader(ServiceRegistry.class).getExtension("zk");
    }

    @Override
    public <T> void pushService(T service) {
        String serviceName = service.getClass().getCanonicalName();
        if (registeredService.contains(serviceName)) {
            return;
        }
        registeredService.add(serviceName);
        Class<?>[] interfaces = service.getClass().getInterfaces();
        for (Class<?> i : interfaces) {
            serviceMap.put(i.getCanonicalName(), service);
        }
        log.info("Add service: {} and interfaces:{}", serviceName, service.getClass().getInterfaces());
        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), NettyRpcServer.PORT);
            //注册到zk
            serviceRegistry.registryService(interfaces[0].getCanonicalName(), inetSocketAddress);
        } catch (UnknownHostException e) {
            throw new RpcException(RpcErrorMessageEnum.REGISTRY_ERROR);
        }
    }

    @Override
    public Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (null == service) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }
}
