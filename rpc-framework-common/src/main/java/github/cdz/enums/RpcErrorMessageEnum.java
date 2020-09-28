package github.cdz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RpcErrorMessageEnum
 *
 * @author chendezhi
 * @date 2020/9/22 18:11
 * @since 1.0.0
 */
@AllArgsConstructor
@Getter
public enum  RpcErrorMessageEnum {

    SERVICE_INVOCATION_FAILURE("服务调用失败"),
    SERVICE_CAN_NOT_BE_NULL("注册的服务不能为空"),
    REQUEST_NOT_MATCH_RESPONSE("requestid不匹配"),
    NO_SEARCH_SERVICE("没有找到服务"),
    REGISTRY_ERROR("注册失败"),
    SERVICE_CAN_NOT_BE_FOUND("服务没有找到"),

    ;

    private String message;
}
