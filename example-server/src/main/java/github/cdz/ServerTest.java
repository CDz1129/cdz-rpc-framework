package github.cdz;

import github.cdz.registry.DefaultServiceRegistry;
import github.cdz.registry.ServiceRegistry;
import github.cdz.socket.RpcServer;

/**
 * ServerTest
 *
 * @author chendezhi
 * @date 2020/9/22 18:34
 * @since 1.0.0
 */
public class ServerTest {

    public static void main(String[] args) {
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        HelloService helloService = new HelloServiceImpl();
        serviceRegistry.registry(helloService);

        RpcServer rpcServer = new RpcServer(serviceRegistry);
        rpcServer.start(9999);
    }
}
