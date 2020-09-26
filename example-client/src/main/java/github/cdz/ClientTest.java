package github.cdz;



import github.cdz.transport.ClientTransport;
import github.cdz.transport.socket.SocketRpcClient;
import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: CDz
 * Create: 2020-09-21 22:22
 **/
@Slf4j
public class ClientTest {

    public static void main(String[] args) {
        ClientTransport rpcClient = new SocketRpcClient("localhost", 9999);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        Hello hello = new Hello("111", "222");
        String s = helloService.hello(hello);
        log.info("helloService 返回：{}",s);
    }
}
