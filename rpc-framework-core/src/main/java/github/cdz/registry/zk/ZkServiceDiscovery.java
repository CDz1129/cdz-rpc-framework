package github.cdz.registry.zk;

import github.cdz.enums.RpcErrorMessageEnum;
import github.cdz.exception.RpcException;
import github.cdz.registry.ServiceDiscovery;
import github.cdz.registry.balance.LoadBalance;
import github.cdz.registry.balance.RandBalance;
import github.cdz.registry.zk.utils.CuratorUtils;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ZkServiceDiscovery
 *
 * @author chendezhi
 * @date 2020/9/28 14:17
 * @since 1.0.0
 * <p>
 * zk实现服务发现(zk中获取 此服务的IP、端口)
 */
public class ZkServiceDiscovery implements ServiceDiscovery {

    private LoadBalance loadBalance;

    public ZkServiceDiscovery() {
        this.loadBalance = new RandBalance();
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<String> addressList = CuratorUtils.getChildrenNodes(zkClient, serviceName);
        if (addressList == null||addressList.size()==0) {
            throw new RpcException(RpcErrorMessageEnum.NO_SEARCH_SERVICE);
        }
        LoadBalance loadBalance = new RandBalance();
        String addrStr = loadBalance.getAddress(addressList);
        String[] split = addrStr.split(":");
        return new InetSocketAddress(split[0],Integer.parseInt(split[1]));
    }
}
