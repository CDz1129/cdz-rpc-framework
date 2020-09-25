package github.cdz;

import github.cdz.registry.DefaultServiceRegistry;
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

        NettyRpcServer nettyRpcServer = new NettyRpcServer(9999);
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        HelloService helloService = new HelloServiceImpl();
        serviceRegistry.registry(helloService);
        nettyRpcServer.run();
    }
}
