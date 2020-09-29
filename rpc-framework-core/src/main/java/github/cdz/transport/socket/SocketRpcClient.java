package github.cdz.transport.socket;

import github.cdz.dto.RpcRequest;
import github.cdz.dto.RpcResponse;
import github.cdz.exception.RpcException;
import github.cdz.transport.ClientTransport;
import github.cdz.utils.checker.RpcMessageChecker;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * RpcClient
 *
 * @author chendezhi
 * @date 2020/9/22 18:02
 * @since 1.0.0
 * client代理 发送请求
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class SocketRpcClient implements ClientTransport {
    //现在还没有使用中间件做 注册中心，所以只能先当作参数传入 host port
    //todo zk注册中心
    private String host;
    private int port;

    @Override
    public RpcResponse sendRpcRequest(RpcRequest rpcRequest) {
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(rpcRequest);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            RpcResponse rpcResponse = (RpcResponse) objectInputStream.readObject();
            return rpcResponse;
        } catch (IOException | ClassNotFoundException e) {
            throw new RpcException("服务调用失败:", e);
        }
    }
}
