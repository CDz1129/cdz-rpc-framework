package github.cdz.transport.socket;

import github.cdz.transport.netty.server.RpcRequestHandle;
import github.cdz.dto.RpcRequest;
import github.cdz.dto.RpcResponse;
import github.cdz.exception.RpcException;
import github.cdz.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
            Object service = serviceRegistry.getService(rpcRequest.getInterfaceName());
            RpcRequestHandle rpcRequestHandle = new RpcRequestHandle();
            RpcResponse rpcResponse = rpcRequestHandle.handle(rpcRequest,service);
            outputStream.writeObject(rpcResponse);
            outputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            //todo 终极问题 ，如果这里又抛出异常，是不能回写的。
            /**
             *  解决办法：
             *  1. 将 try-with-resource修改为 try-catch-finally
             *  2. 见这一块全部抽象，抽象出来一个方法处理 负责将 rpcRequest转化为 rpcResponse
             *      ——就算是使用handler转化request还是不能捕捉到所有异常
             *  todo 使用第二种方式
              */
            throw new RpcException("RPC异常", e);
        }
    }


}
