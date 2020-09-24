package github.cdz;

import github.cdz.RpcClient;
import github.cdz.dto.RpcRequest;
import github.cdz.transport.socket.SocketRpcClient;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * RpcClientProxy
 *
 * @author chendezhi
 * @date 2020/9/22 18:02
 * @since 1.0.0
 *
 * client 代理类
 */
@Slf4j
public class RpcClientProxy implements InvocationHandler {


    private RpcClient rpcClient;

    public RpcClientProxy(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    /**
     * 为什么JDK动态代理只能代理有接口的类？
     *
     * 因为生成的代理对象是 继承了 Proxy类，Java没有多继承，只能实现接口
     *
     * @param proxy
     * @param method
     * @param args
     * @return
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        log.info("client:在invoke中[" + method.getName() + "]方法被调用");
        //socket 使用socket发送请求——client端
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes()).build();
        return rpcClient.sendRpcRequest(rpcRequest);

    }
}
