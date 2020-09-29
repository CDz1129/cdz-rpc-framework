package github.cdz.transport.netty.client;

import github.cdz.dto.RpcRequest;
import github.cdz.dto.RpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * UnprocessedRequests
 *
 * @author chendezhi
 * @date 2020/9/29 17:37
 * @since 1.0.0
 * 未完成的request
 */
public class UnprocessedRequests {

    private static Map<String, CompletableFuture<RpcResponse>> unprocessedResponseFutures = new ConcurrentHashMap<>();

    public void put(String requestId, CompletableFuture<RpcResponse> future){
        unprocessedResponseFutures.put(requestId,future);
    }

    public void complete(RpcResponse rpcResponse){
        CompletableFuture<RpcResponse> future = unprocessedResponseFutures.remove(rpcResponse.getRequestId());
        if (null!=future){
            future.complete(rpcResponse);
        }else {
            throw new IllegalStateException();
        }
    }

    public void remove(String requestId) {
        unprocessedResponseFutures.remove(requestId);
    }
}
