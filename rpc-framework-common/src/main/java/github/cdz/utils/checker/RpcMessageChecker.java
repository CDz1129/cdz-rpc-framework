package github.cdz.utils.checker;

import github.cdz.dto.RpcRequest;
import github.cdz.dto.RpcResponse;
import github.cdz.enums.RpcErrorMessageEnum;
import github.cdz.enums.RpcResponseCode;
import github.cdz.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * RpcMessageChecker
 *
 * @author chendezhi
 * @date 2020/9/25 19:08
 * @since 1.0.0
 */
@Slf4j
public class RpcMessageChecker {

    private void rpcMessageChecker() {
    }

    public static void check(RpcRequest rpcRequest, RpcResponse rpcResponse) {
        if (rpcResponse == null) {
            log.info("rpc 调用失败:{}", rpcRequest.getInterfaceName());
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, "interfaceName:" + rpcRequest.getInterfaceName());
        }
        if (!Objects.equals(rpcRequest.getRequestId(), rpcResponse.getRequestId())) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, "interfaceName:" + rpcRequest.getInterfaceName());
        }
        if (rpcResponse.getCode() == null || !rpcResponse.getCode().equals(RpcResponseCode.SUCCESS.getCode())) {
            log.info("rpc 调用失败:{},rpcResponse:{}", rpcRequest.getInterfaceName(),rpcResponse);
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, "interfaceName:" + rpcRequest.getInterfaceName());
        }
    }

}
