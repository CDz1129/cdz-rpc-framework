package github.cdz;

import github.cdz.dto.RpcRequest;
import github.cdz.dto.RpcResponse;
import github.cdz.transport.ClientTransport;
import github.cdz.transport.netty.client.NettyClientTransport;
import github.cdz.transport.socket.SocketRpcClient;
import github.cdz.utils.checker.RpcMessageChecker;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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


    private ClientTransport clientTransport;

    public RpcClientProxy(ClientTransport clientTransport) {
        this.clientTransport = clientTransport;
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
    @SneakyThrows
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        log.info("client:在invoke中[" + method.getName() + "]方法被调用");
        //socket 使用socket发送请求——client端
        RpcRequest rpcRequest = RpcRequest.builder()
                .requestId(UUID.randomUUID().toString())
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes()).build();
        RpcResponse rpcResponse = null;
        if (clientTransport instanceof NettyClientTransport) {
            CompletableFuture<RpcResponse> o = (CompletableFuture<RpcResponse>)clientTransport.sendRpcRequest(rpcRequest);
            rpcResponse = o.get();
        }
        if (clientTransport instanceof SocketRpcClient){
            rpcResponse = (RpcResponse)clientTransport.sendRpcRequest(rpcRequest);
        }
        RpcMessageChecker.check(rpcRequest,rpcResponse);
        return rpcResponse.getData();

    }
}
