package github.cdz;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: CDz
 * Create: 2020-09-21 22:22
 **/
public class ClientTest {

    public static void main(String[] args) {
        ClientProxy clientProxy = new ClientProxy();
        HelloService helloService = clientProxy.getProxy(HelloService.class);
        String hello = helloService.hello("111");
        System.out.println(hello);
    }
}
