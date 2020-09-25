package github.cdz.transport.netty.client;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ChannelProvider
 *
 * @author chendezhi
 * @date 2020/9/25 19:22
 * @since 1.0.0
 *
 * channel提供者，负责缓存channel 与对外提供Chanel
 */
public class ChannelProvider {

    private static final Map<String, Channel> channelGroup = new ConcurrentHashMap<>();

}
