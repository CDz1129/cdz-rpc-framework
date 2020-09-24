package github.cdz.registry;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DefaultServiceRegistry
 *
 * @author chendezhi
 * @date 2020/9/23 10:19
 * @since 1.0.0
 *
 * 默认 使用map实现注册
 */
@Slf4j
public class DefaultServiceRegistry implements ServiceRegistry{

    //key:接口名称
    //value:服务
    private final Map<String,Object> serviceMap = new ConcurrentHashMap<>();
    private final Set<String> registryService = ConcurrentHashMap.newKeySet();

    //todo 自动扫描
    @Override
    public <T> void registry(T service) {
        //1. 判断是否注册过
        String serviceName = service.getClass().getCanonicalName();
        if (registryService.contains(serviceName)){
            //是否注册过，如果注册过，直接跳过
            return;
        }
        //没有注册过 添加入 registryService ->set中
        registryService.add(serviceName);

        //2. 添加入 servicemap key为 interface name
        Class<?>[] interfaces = service.getClass().getInterfaces();
        for (Class<?> i : interfaces) {
            serviceMap.put(i.getCanonicalName(),service);
        }
        log.info("add service :{} and interface :{}",serviceName,interfaces);
    }

    @Override
    public Object getService(String serviceName) {
        return serviceMap.get(serviceName);
    }

    public static void main(String[] args) {
        DefaultServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        String canonicalName = serviceRegistry.getClass().getCanonicalName();
        //getCanonicalName 获取全限定名
        System.out.println(canonicalName);
        Class<?>[] interfaces = serviceRegistry.getClass().getInterfaces();
        // getInterfaces() 获取所有得接口 -> getCanonicalName() 接口得全限定名
        for (Class<?> anInterface : interfaces) {
            System.out.println(anInterface.getCanonicalName());
        }
    }

}
