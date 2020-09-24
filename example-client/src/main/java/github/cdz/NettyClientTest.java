package github.cdz;

import github.cdz.transport.netty.NettyRpcClient;
import lombok.extern.slf4j.Slf4j;

/**
 * NettyClientTest
 *
 * @author chendezhi
 * @date 2020/9/24 17:07
 * @since 1.0.0
 */
@Slf4j
public class NettyClientTest {

    public static void main(String[] args) {
        RpcClient rpcClient = new NettyRpcClient("localhost", 9999);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        Hello hello = new Hello("111", "222");
        String s = helloService.hello(hello);
        log.info("helloService 返回：{}",s);
    }
}
