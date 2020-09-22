package github.cdz;

import lombok.*;

import java.io.Serializable;

/**
 * Hello
 *
 * @author chendezhi
 * @date 2020/9/22 18:21
 * @since 1.0.0
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Hello implements Serializable {
    private String aa;
    private String bb;
}
