package github.cdz.client;

import github.cdz.RpcRequest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: CDz
 * Create: 2020-09-21 22:28
 * <p>
 * 类反射代理生成器
 * <p>
 * InvocationHandler 顾名思义，handler处理器
 * 所有方法执行 invoke都会加入逻辑
 **/
public class ClientProxy implements InvocationHandler {

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
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("在invoke中[" + method.getName() + "]方法被调用");
        //socket 使用socket发送请求——client端
        try (Socket socket = new Socket("localhost", 9999)){
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            RpcRequest rpcRequest = RpcRequest.builder()
                    .interfaceName(method.getDeclaringClass().getName())
                    .methodName(method.getName())
                    .parameters(args)
                    .paramTypes(method.getParameterTypes()).build();
            objectOutputStream.writeObject(rpcRequest);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            return objectInputStream.readObject();
        }
    }
}
