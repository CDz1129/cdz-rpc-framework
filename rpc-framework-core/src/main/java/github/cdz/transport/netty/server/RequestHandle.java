package github.cdz.transport.netty.server;

import github.cdz.dto.RpcRequest;
import github.cdz.dto.RpcResponse;

/**
 * RequestHandle
 *
 * @author chendezhi
 * @date 2020/9/24 16:22
 * @since 1.0.0
 */
public interface RequestHandle {
    RpcResponse handle(RpcRequest rpcRequest, Object service);
}
