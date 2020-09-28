package github.cdz.registry.balance;

import org.apache.commons.lang.math.RandomUtils;

import java.util.List;

/**
 * RandBalance
 *
 * @author chendezhi
 * @date 2020/9/28 14:30
 * @since 1.0.0
 */
public class RandBalance implements LoadBalance {
    @Override
    public String getAddress(List<String> addList) {
        return addList.get(RandomUtils.nextInt(addList.size()));
    }
}
