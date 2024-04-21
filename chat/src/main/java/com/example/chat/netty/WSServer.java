package com.example.chat.netty;

import com.example.chat.mapper.jpa.GroupUserMapper;
import com.example.core.route.ModulePort;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// @Component
public class WSServer {

    private GroupUserMapper groupUserMapper;

    private EventLoopGroup mainGroup;
    private EventLoopGroup subGroup;
    private ServerBootstrap server;
    private ChannelFuture future;

//    @Autowired
    public WSServer(GroupUserMapper groupUserMapper) {
        this.groupUserMapper = groupUserMapper;
        mainGroup = new NioEventLoopGroup();
        subGroup = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(mainGroup, subGroup) //
                .channel(NioServerSocketChannel.class) //
                .childHandler(new WSServerInitializer(groupUserMapper));
    }

    public void start() {
        this.future = server.bind(ModulePort.NETTY.getType());
        System.err.println("netty websocket server 启动完毕...");
    }
}
