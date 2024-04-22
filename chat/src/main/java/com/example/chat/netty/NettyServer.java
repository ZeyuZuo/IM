//package com.example.chat.netty;
//
//import com.example.chat.netty.element.ChatMsg;
//import com.example.chat.netty.element.Content;
//import com.example.core.route.ModulePort;
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelFutureListener;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.PreDestroy;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import java.net.InetSocketAddress;
//
//
//@Component
//public class NettyServer {
//    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
//    private final ServerBootstrap serverBootstrap = new ServerBootstrap();
//    private ChannelFuture future;
//    private final EventLoopGroup boss = new NioEventLoopGroup();
//    private final EventLoopGroup work = new NioEventLoopGroup();
//
//
//    @PostConstruct
//    public void start() throws Exception{
//        serverBootstrap
//                .group(boss, work)
//                .channel(NioServerSocketChannel.class)
//                .localAddress(new InetSocketAddress(ModulePort.NETTY.getType()))
//                .childHandler(new NettyServerInit());
//        this.future = serverBootstrap.bind().sync();
//        if(this.future.isSuccess()){
//            System.out.println("netty服务器启动成功");
//            logger.debug("netty服务器启动");
//        }else{
//            logger.debug("netty服务器启动失败");
//            System.out.println("netty服务器启动失败");
//        }
//    }
//
//    @PreDestroy
//    public void destroy() {
//        boss.shutdownGracefully().syncUninterruptibly();
//        work.shutdownGracefully().syncUninterruptibly();
//        System.out.println("netty服务器关闭");
//        logger.debug("NettyServer已关闭");
//    }
//
//    public void sendMsg(Content content){
//        ChatMsg chatMsg = content.getChatMsg();
//        Channel receiveChannel = UserChannel.get(chatMsg.getReceiver());
//        if(receiveChannel==null){
//            System.out.printf("%s不在线",chatMsg.getReceiver());
//            // return;
//        }
////        try{
////            // ChannelFuture future = receiveChannel.writeAndFlush(content);
////
////            future.addListener((ChannelFutureListener) channelFuture ->
////                    logger.debug("服务端转发Goolgel protocol 成功 ：{}", content.toString())
////            );
////        }catch(NullPointerException e){
////            logger.debug("nullPointException when sendMsg");
////            System.out.println("nullPointException when sendMsg");
////        }
//    }
//}
