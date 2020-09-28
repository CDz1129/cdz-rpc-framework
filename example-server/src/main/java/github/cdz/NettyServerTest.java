package github.cdz;

import github.cdz.provider.ServiceProvider;
import github.cdz.provider.ServiceProviderImpl;
import github.cdz.registry.ServiceRegistry;
import github.cdz.transport.netty.server.NettyRpcServer;
import lombok.extern.slf4j.Slf4j;

/**
 * NettyServerTest
 *
 * @author chendezhi
 * @date 2020/9/24 17:07
 * @since 1.0.0
 */
@Slf4j
public class NettyServerTest {
    public static void main(String[] args) {
        NettyRpcServer nettyRpcServer = new NettyRpcServer();
        HelloService helloService = new HelloServiceImpl();

        ServiceProvider serviceProvider = new ServiceProviderImpl();
        serviceProvider.pushService(helloService);

        nettyRpcServer.run();
    }
}
