package github.cdz;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("在invoke中[" + method.getName() + "]方法被调用");

        return "1";
    }
}
