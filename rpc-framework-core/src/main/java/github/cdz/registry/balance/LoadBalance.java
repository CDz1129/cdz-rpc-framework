package github.cdz.registry.balance;

import java.util.List;

/**
 * LoadBalance
 *
 * @author chendezhi
 * @date 2020/9/28 14:29
 * @since 1.0.0
 */
public interface LoadBalance {

    String getAddress(List<String> addList);
}
