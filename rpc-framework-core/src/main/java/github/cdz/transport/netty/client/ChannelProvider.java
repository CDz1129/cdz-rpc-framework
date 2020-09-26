package github.cdz.transport.netty.client;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ChannelProvider
 *
 * @author chendezhi
 * @date 2020/9/25 19:22
 * @since 1.0.0
 * <p>
 * channel提供者，负责缓存channel 与对外提供Chanel
 */
public class ChannelProvider {

    private static final Map<String, Channel> channelMap = new ConcurrentHashMap<>();
    private static final NettyRpcClient nettyRpcClient = new NettyRpcClient();

    public Channel get(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        if (channelMap.containsKey(key)) {
            Channel channel = channelMap.get(key);
            if (channel != null && channel.isActive()) {
                return channel;
            } else {
                channelMap.remove(key);
            }
        }
        Channel channel = nettyRpcClient.doConnect(inetSocketAddress);
        channelMap.put(key, channel);
        return channel;
    }
}
