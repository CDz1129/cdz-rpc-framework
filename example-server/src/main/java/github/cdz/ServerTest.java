package github.cdz;

import github.cdz.provider.ServiceProvider;
import github.cdz.provider.ServiceProviderImpl;
import github.cdz.registry.ServiceRegistry;
import github.cdz.transport.socket.SocketRpcServer;

/**
 * ServerTest
 *
 * @author chendezhi
 * @date 2020/9/22 18:34
 * @since 1.0.0
 */
public class ServerTest {

    public static void main(String[] args) {

        HelloService helloService = new HelloServiceImpl();

        ServiceProvider serviceProvider = new ServiceProviderImpl();
        serviceProvider.pushService(helloService);

        SocketRpcServer rpcServer = new SocketRpcServer(serviceProvider);
        rpcServer.start(9999);
    }
}
