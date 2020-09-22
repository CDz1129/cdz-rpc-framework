package github.cdz;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Request
 *
 * @author chendezhi
 * @date 2020/9/22 15:18
 * @since 1.0.0
 */
@Data
@Builder
public class RpcRequest implements Serializable {
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;
}
