package github.cdz.dto;

import github.cdz.enums.RpcResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * RpcResponse
 *
 * @author chendezhi
 * @date 2020/9/22 17:36
 * @since 1.0.0
 * 返回响应
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse<T> implements Serializable {

    private String requestId;
    private String msg;
    private Integer code;
    private T data;

    public static <T>RpcResponse<T> success(T data,String requestId){
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(RpcResponseCode.SUCCESS.getCode());
        response.setRequestId(requestId);
        if (null!=data){
            response.setData(data);
        }
        return response;
    }

    public static <T> RpcResponse<T> fail(RpcResponseCode rpcResponseCode){
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(rpcResponseCode.getCode());
        response.setMsg(rpcResponseCode.getMessage());
        return response;
    }

}


