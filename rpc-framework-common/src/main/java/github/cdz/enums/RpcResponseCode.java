package github.cdz.enums;

import lombok.Getter;

/**
 * RpcResponseCode
 *
 * @author chendezhi
 * @date 2020/9/22 17:38
 * @since 1.0.0
 */
@Getter
public enum RpcResponseCode {

    SUCCESS(200,"调用成功"),
    FAIL(500,"调用失败"),
    NOT_FOUNT_CLASS(500,"未找到指定类"),
    NOT_FOUNT_METHOD(500,"未找到指定方法"),

    ;


    RpcResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;
}
