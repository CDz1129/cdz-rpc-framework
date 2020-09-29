package github.cdz.transport;

import github.cdz.dto.RpcRequest;
import github.cdz.dto.RpcResponse;

/**
 * ClientTransport
 *
 * @author chendezhi
 * @date 2020/9/26 10:44
 * @since 1.0.0
 *
 * 合并 RpcClient/RequestHandle接口
 *
 */
public interface ClientTransport {

    /**
     *
     * @param rpcRequest rpc request body
     * @return 服务器返回data
     */
    Object sendRpcRequest(RpcRequest rpcRequest);
}
