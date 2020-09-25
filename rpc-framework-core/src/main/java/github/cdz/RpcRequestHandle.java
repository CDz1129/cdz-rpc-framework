package github.cdz;

import github.cdz.RequestHandle;
import github.cdz.dto.RpcRequest;
import github.cdz.dto.RpcResponse;
import github.cdz.enums.RpcResponseCode;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * RpcRequestHandle
 *
 * @author chendezhi
 * @date 2020/9/24 11:40
 * @since 1.0.0
 */
@Slf4j
public class RpcRequestHandle implements RequestHandle {

    @Override
    public RpcResponse handle(RpcRequest rpcRequest, Object service) {
        RpcResponse rpcResponse = invokeTargetMethod(rpcRequest, service);
        return rpcResponse;
    }

    private RpcResponse invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        if (service == null) {
            //RPC异常
            log.error("没有找到实体类:{}", rpcRequest.getInterfaceName());
            return RpcResponse.fail(RpcResponseCode.NOT_FOUNT_CLASS);
        }
        Method method = null;
        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
        } catch (NoSuchMethodException e) {
            //RPC异常
            log.error("没有找到方法", e);
            return RpcResponse.fail(RpcResponseCode.NOT_FOUNT_METHOD);
        }
        Object ret = null;
        try {
            ret = method.invoke(service, rpcRequest.getParameters());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            //RPC业务异常
            log.error("invoke {} 方法异常", rpcRequest.getInterfaceName(), e);
            return RpcResponse.fail(RpcResponseCode.FAIL);
        }
        return RpcResponse.success(ret,rpcRequest.getRequestId());
    }
}
