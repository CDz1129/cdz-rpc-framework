package github.cdz;

import github.cdz.dto.RpcRequest;

/**
 * RpcClient
 *
 * @author chendezhi
 * @date 2020/9/24 16:32
 * @since 1.0.0
 */
public interface RpcClient {
    Object sendRpcRequest(RpcRequest rpcRequest);
}
