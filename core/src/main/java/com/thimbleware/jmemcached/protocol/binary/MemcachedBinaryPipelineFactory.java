package com.thimbleware.jmemcached.protocol.binary;

import com.thimbleware.jmemcached.CacheImpl;
import com.thimbleware.jmemcached.protocol.MemcachedCommandHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.group.DefaultChannelGroup;


public class MemcachedBinaryPipelineFactory implements ChannelPipelineFactory {

    private CacheImpl cache;
    private String version;
    private boolean verbose;
    private int idleTime;

    private DefaultChannelGroup channelGroup;

    public MemcachedBinaryPipelineFactory(CacheImpl cache, String version, boolean verbose, int idleTime, DefaultChannelGroup channelGroup) {
        this.cache = cache;
        this.version = version;
        this.verbose = verbose;
        this.idleTime = idleTime;
        this.channelGroup = channelGroup;
    }

    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addLast("commandDecoder", new MemcachedBinaryCommandDecoder());
        pipeline.addAfter("commandDecoder", "commandHandler", new MemcachedCommandHandler(cache, version, verbose, idleTime, channelGroup));
        pipeline.addAfter("commandHandler", "responseEncoder", new MemcachedBinaryResponseEncoder());

        return pipeline;
    }
}
