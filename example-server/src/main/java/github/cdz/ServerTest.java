package github.cdz;

/**
 * ServerTest
 *
 * @author chendezhi
 * @date 2020/9/22 18:34
 * @since 1.0.0
 */
public class ServerTest {

    public static void main(String[] args) {
        RpcServer rpcServer = new RpcServer();
        HelloService helloService = new HelloServiceImpl();
        rpcServer.register(helloService,9999);
    }
}
