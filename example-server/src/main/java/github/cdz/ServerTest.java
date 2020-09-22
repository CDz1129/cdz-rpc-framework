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
        System.out.println("二次注册，下面不会执行");
        rpcServer.register(new HelloServiceImpl(),9999);
    }
}
