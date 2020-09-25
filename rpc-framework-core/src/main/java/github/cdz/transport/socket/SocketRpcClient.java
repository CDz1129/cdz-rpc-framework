package github.cdz.transport.socket;

import github.cdz.RpcClient;
import github.cdz.dto.RpcRequest;
import github.cdz.dto.RpcResponse;
import github.cdz.enums.RpcErrorMessageEnum;
import github.cdz.enums.RpcResponseCode;
import github.cdz.exception.RpcException;
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
public class SocketRpcClient implements RpcClient {
    private String host;
    private int port;

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(rpcRequest);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            RpcResponse rpcResponse = (RpcResponse) objectInputStream.readObject();
            RpcMessageChecker.check(rpcRequest, rpcResponse);
            return rpcResponse.getData();
        } catch (IOException | ClassNotFoundException e) {
            throw new RpcException("服务调用失败:", e);
        }
    }
}
