package github.cdz.exception;

import github.cdz.enums.RpcErrorMessageEnum;

/**
 * RpcException
 *
 * @author chendezhi
 * @date 2020/9/22 18:09
 * @since 1.0.0
 */
public class RpcException extends RuntimeException {


    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum, String detail) {
        super(rpcErrorMessageEnum.getMessage() + ":" + detail);
    }

    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum) {
        super(rpcErrorMessageEnum.getMessage());
    }


    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }
}
