package github.cdz;

import github.cdz.transport.ClientTransport;
import github.cdz.transport.netty.client.NettyClientTransport;
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
        ClientTransport clientTransport = new NettyClientTransport();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(clientTransport);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        Hello hello = new Hello("111", "222");
        String s = helloService.hello(hello);
        log.info("helloService 返回：{}",s);
    }
}
