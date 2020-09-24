package github.cdz.socket;

import github.cdz.dto.RpcRequest;
import github.cdz.dto.RpcResponse;
import github.cdz.enums.RpcErrorMessageEnum;
import github.cdz.enums.RpcResponseCode;
import github.cdz.exception.RpcException;
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
public class RpcClient {
    public Object sendRpcRequest(RpcRequest rpcRequest, String host, int port) {
        try (Socket socket = new Socket(host, port)){
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(rpcRequest);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            RpcResponse rpcResponse = (RpcResponse)objectInputStream.readObject();
            if (rpcResponse==null){
                log.info("rpc 调用失败:{}",rpcRequest.getInterfaceName());
                throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, "interfaceName:" + rpcRequest.getInterfaceName());
            }
            if (rpcResponse.getCode()==null||!rpcResponse.getCode().equals(RpcResponseCode.SUCCESS.getCode())){
                log.info("rpc 调用失败:{}",rpcRequest.getInterfaceName());
                throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, "interfaceName:" + rpcRequest.getInterfaceName());
            }
            return rpcResponse.getData();
        } catch (IOException | ClassNotFoundException e) {
            throw new RpcException("服务调用失败:",e);
        }
    }
}