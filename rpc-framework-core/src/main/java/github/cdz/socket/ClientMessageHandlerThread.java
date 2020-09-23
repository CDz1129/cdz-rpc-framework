package github.cdz.socket;

import github.cdz.dto.RpcRequest;
import github.cdz.dto.RpcResponse;
import github.cdz.enums.RpcResponseCode;
import github.cdz.exception.RpcException;
import github.cdz.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * ClientMessageHandlerThread
 * <p>
 * server处理socket类
 *
 * @author chendezhi
 * @date 2020/9/22 17:47
 * @since 1.0.0
 */
@Slf4j
public class ClientMessageHandlerThread implements Runnable {


    private Socket socket;
    private ServiceRegistry serviceRegistry;

    public ClientMessageHandlerThread(Socket socket, ServiceRegistry serviceRegistry) {
        this.socket = socket;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void run() {
        try (ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest) inputStream.readObject();
            RpcResponse obj = invokeTargetMethod(rpcRequest);
            outputStream.writeObject(obj);
            outputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            //todo 终极问题 ，如果这里又抛出异常，是不能回写的。
            /**
             *  解决办法：
             *  1. 将 try-with-resource修改为 try-catch-finally
             *  2. 见这一块全部抽象，抽象出来一个方法处理 负责将 rpcRequest转化为 rpcResponse
             *  todo 使用第二种方式
              */
            throw new RpcException("RPC异常", e);
        }
    }

    //todo 返回处理
    // 这里有个疑问——调用报错到底需要不需要返回错误？返回什么样得错误？
    // 这个错误，到底在客户端应该是一个怎么样得表现形式？
    // 在server端又应该是怎么样得表现形式？
    // 我的理解是 感觉客户端只需要知道是那个RPC接口出现了问题就好了
    // 当知道是那个RPC接口后，再到对应得接口去服务去查询日志即可
    // 带着疑问搜索了一下-》https://tech.antfin.com/docs/2/46953 蚂蚁金服给了一个定义
    // 总结来看，更具体化了我的想法：
    // RPC返回，不返回具体错误信息，只返回概要（成功、RPC逻辑错误，业务逻辑错误）
    // 真正的错误详情再service提供端打印出日志
    private RpcResponse invokeTargetMethod(RpcRequest rpcRequest) {

        Object service = serviceRegistry.getService(rpcRequest.getInterfaceName());
        if (service == null) {
            //RPC异常
            log.error("没有找到实体类:{}", rpcRequest.getInterfaceName());
            return RpcResponse.fail(RpcResponseCode.NOT_FOUNT_CLASS);
        }
        Method method = null;
        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
        } catch (NoSuchMethodException e) {
            //RPC异常
            log.error("没有找到方法", e);
            return RpcResponse.fail(RpcResponseCode.NOT_FOUNT_METHOD);
        }
        Object ret = null;
        try {
            ret = method.invoke(service, rpcRequest.getParameters());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            //RPC业务异常
            log.error("invoke {} 方法异常", rpcRequest.getInterfaceName(), e);
            return RpcResponse.fail(RpcResponseCode.FAIL);
        }
        return RpcResponse.success(ret);
    }
}
