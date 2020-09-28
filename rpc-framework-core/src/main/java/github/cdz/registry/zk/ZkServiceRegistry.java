package github.cdz.registry.zk;

import github.cdz.registry.ServiceRegistry;
import github.cdz.registry.zk.utils.CuratorUtils;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;

/**
 * ZkServiceRegistry
 *
 * @author chendezhi
 * @date 2020/9/28 14:17
 * @since 1.0.0
 */
public class ZkServiceRegistry implements ServiceRegistry {

    @Override
    public void registryService(String serviceName, InetSocketAddress inetSocketAddress) {
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        String path = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/" + serviceName + inetSocketAddress.toString();
        CuratorUtils.createPersistentNode(zkClient,path);
    }
}
