package github.cdz;

import github.cdz.dto.RpcRequest;
import github.cdz.dto.RpcResponse;
import github.cdz.enums.RpcResponseCode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * ClientMessageHandlerThread
 *
 * 处理socket类
 *
 * @author chendezhi
 * @date 2020/9/22 17:47
 * @since 1.0.0
 */
@Slf4j
public class ClientMessageHandlerThread implements Runnable {


    private Socket socket;
    private Object service;

    public ClientMessageHandlerThread(Socket socket, Object service) {
        this.socket = socket;
        this.service = service;
    }

    @Override
    public void run() {
        try (ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest) inputStream.readObject();
            RpcResponse obj = invokeTargetMethod(rpcRequest);
            outputStream.writeObject(obj);
            outputStream.flush();
        } catch (IOException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private RpcResponse invokeTargetMethod(RpcRequest rpcRequest) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> clazz = Class.forName(rpcRequest.getInterfaceName());
        if (!clazz.isAssignableFrom(service.getClass())){
            return RpcResponse.fail(RpcResponseCode.NOT_FOUNT_CLASS);
        }
        Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
        if (null==method){
            return RpcResponse.fail(RpcResponseCode.NOT_FOUNT_METHOD);
        }
        Object ret = method.invoke(service, rpcRequest.getParameters());
        return RpcResponse.success(ret);
    }
}
