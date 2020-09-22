
想要手写一个RPC，发现前置知识还是挺多的。而这些知识大部分我都不太熟悉，导致参考[guide-rpc-framework](https://github.com/Snailclimb/guide-rpc-framework)，
也看不懂。

最后找到一个好的办法，直接查看`git history`，任何代码都是又简到复杂的，那么就从最简单的开始。 

## 为什么要用动态代理？

`InvocationHandler`类配合`Proxy`类，实现代理效果。

其实和AOP非常相似，为什么需要？

仔细想一下RPC调用过程，client端需要想本地调用方法一样，调用远程服务（而我们写代码上不用关系这些，只需要调用想要的方法即可）。

**那么就需要，client端的本地方法，被代理。方法实现是去网络请求，然后再返回数据，作为return。**

如何实现？

JDK 动态代理：

- InvocationHandler
- Proxy

## 如何使用动态代理？

1. 创建一个接口：
```java
public interface HelloService {
    public String hello(String s);
}
```

2. 实现接口类：

```java
public class HelloServiceImpl implements HelloService {
    public String hello(String s) {
        return "say:"+s;
    }
}
```

3. 代理类实现`InvocationHandler`

```java
public class ClientProxy implements InvocationHandler {

    private Object p = new HelloServiceImpl();

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    /**
     * 为什么JDK动态代理只能代理有接口的类？
     *
     * 因为生成的代理对象是 继承了 Proxy类，Java没有多继承，只能实现接口
     *
     * @param proxy 代理的类
     * @param method 执行的方法
     * @param args 方法参数
     * @return
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("在invoke中[" + method.getName() + "]方法被调用");
        //实际执行的是，内部HelloServiceImpl实例方法
        //而RPC的话，我们可以在这里构造请求并返回
        return method.invoke(p,args);
    }
}
```

这个类是关键：

可以看到`InvocationHandler`实现后需要实现`invoke`方法。这个方法可以看作是所有方法的代理（AOP）。
这里只是demo，写死了`method.invoke(p,args)`的类。

获取实例，是通过`Proxy.newProxyInstance(ClassLoader loader,Class<?>[] interfaces,InvocationHandler h)`得到一个具体的代理类，
可以看到，最后一个参数，必须要实现了InvocationHandler的类。

仔细想一下，其实在RPC中，client就是本地调用的，就是*接口的方法*，所以实现的

实验：
```java
public class ClientTest {
    public static void main(String[] args) {
        ClientProxy clientProxy = new ClientProxy();
        HelloService helloService = clientProxy.getProxy(HelloService.class);
        String hello = helloService.hello("111");
        System.out.println(hello);
    }
}
```
结果：
```
在invoke中[hello]方法被调用
say:111
```

*可以看到，invoke是在代理方法之前得。

## 总结：

JDK动态代理实现类似AOP功能方式：

- 实现 InvocationHandler接口，重写invoke方法
- 使用 Proxy.newProxyInstance 创建实例
