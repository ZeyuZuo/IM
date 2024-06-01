package com.example.chat.netty.handler;

import com.example.chat.netty.UserChannel;
import com.example.core.element.ChatActionEnum;
import com.example.core.element.ChatMsg;
import com.example.core.element.ConnectNetty;
import com.example.core.element.Content;
import com.example.core.utils.IpUtils;
import com.example.core.utils.JsonUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Objects;


@Component
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private final Logger logger = LoggerFactory.getLogger(ChatHandler.class);

    private Jedis jedis;

    @Autowired
    public ChatHandler(Jedis jedis){
        this.jedis = jedis;
    }


//    private void sendChatMsg(ChatMsg chatMsg, Channel receiverChannel){
//
//        if(receiverChannel == null){
//            logger.debug("receiver {} offline",chatMsg.getReceiver());
//            System.out.println("receiver offline");
//            return;
//        }
//
//        String sender = chatMsg.getSender();
//        String receiver = chatMsg.getReceiver();
//        String msgText = chatMsg.getMsg();
//
//        // Channel receiverChannel = UserChannel.get(receiver);
//        if (receiverChannel == null) {
//            System.out.println("receiver offline");
//        } else {
//            Channel findChannel = users.find(receiverChannel.id());
//            if (findChannel != null) {
//                receiverChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.obj2Json(chatMsg)));
//            }
//        }
//    }
//
//    private void send(Object s, Channel receiverChannel){
//        if(receiverChannel==null){
//            logger.debug("receiver offline");
//            System.out.println("receiver offline");
//        }
//
//        Channel findChannel = users.find(receiverChannel.id());
//        if (findChannel != null) {
//            receiverChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.obj2Json(s)));
//        }
//    }

    @Override
    protected synchronized void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        String text = msg.text();
        System.out.println("接收到的数据为：" + text);
        Channel currentChannel = ctx.channel();

        ConnectNetty connectNetty;
        // Content content = null;

        try{
            connectNetty = JsonUtils.json2Obj(text, ConnectNetty.class);
            Integer action = connectNetty.getAction();
            String connector = connectNetty.getSender();
            String receiver = connectNetty.getReceiver();
            if(Objects.equals(action, ChatActionEnum.CONNECT.getType())){
                System.out.println("----------connecting----------");
            }else{
                System.out.println("wrong action");
                return;
            }
            if(Objects.equals(receiver,"server")){
                System.out.println("----------server----------");
            }else {
                System.out.println("wrong receiver");
                return;
            }
            jedis.set(connector + "Netty", IpUtils.getPrivateIp());
            jedis.set(currentChannel.id().asLongText(), connector);
            System.out.println("connect success!");
            UserChannel.put(connector, currentChannel);
        }catch(Exception e){
            logger.debug("消息转换失败");
            System.out.println("消息转换失败");
        }



//        // 2. 判断消息类型，根据不同的类型来处理不同的业务
//        if (Objects.equals(action, ChatActionEnum.CONNECT.getType())) {
//            System.out.println("connecting");
//            ChatMsg chatMsg = content.getChatMsg();
//            if (Objects.equals("server", chatMsg.getReceiver())) {
//                String sender = chatMsg.getSender();
//
//                jedis.set(sender + "Netty", IpUtils.getPrivateIp());
//                jedis.set(currentChannel.id().asLongText(), sender);
//                System.out.println("connect success!");
//                jedis.set(sender + "Channel", currentChannel.id().asLongText());
//                for (Channel channel : users) {
//                    System.out.println(channel.id().asLongText());
//                }
//                UserChannel.output();
//            } else {
//                System.out.println("连接服务器失败");
//            }
//        }
//        } else if (Objects.equals(action, ChatActionEnum.CHAT.getType())) {
//            ChatMsg chatMsg = content.getChatMsg();
//            Channel receiverChannel = UserChannel.get(content.getChatMsg().getReceiver());
//            if(Objects.equals("empty",chatMsg.getGroup())){
//                sendChatMsg(chatMsg, receiverChannel);
//            }
//        } else if(Objects.equals(action, ChatActionEnum.GROUPCHAT.getType())) {
//            ChatMsg chatMsg = content.getChatMsg();
//            String sender = chatMsg.getSender();
//            String group = chatMsg.getGroup();
//            System.out.println(group);
//
//            List<GroupUserPo> poList = groupUserMapper.findByGroup(group);
//
//            for(GroupUserPo po : poList){
//                // System.out.println(po.getUser());
//                String receiver = po.getUser();
//                Channel receiverChannel = UserChannel.get(receiver);
//                if(!Objects.equals(receiver,sender)){
//                    chatMsg.setReceiver(receiver);
//                    sendChatMsg(chatMsg, receiverChannel);
//                }
//            }
//        }
    }

        /**
         * 当客户端连接服务端之后（打开连接）
         * 获取客户端的channel，并且放到ChannelGroup中去进行管理
         */
    @Override
    public synchronized void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("add a client");
        users.add(ctx.channel());
    }

    @Override
    public synchronized void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

        String channelId = ctx.channel().id().asLongText();
        System.out.println("客户端被移除，channelId为：" + channelId);

        // 当触发handlerRemoved，ChannelGroup会自动移除对应客户端的channel
        users.remove(ctx.channel());
        String sender = jedis.get(channelId);
//        try(JedisPool tmp = RedisPool.getPool()){
//            Jedis jedis = tmp.getResource();
//            jedis.del(channelId);
//            System.out.println("jedis del " + channelId);
//            jedis.del(sender + "Netty");
//            System.out.println("jedis del " + sender + "Netty");
//        }
        jedis.del(channelId);
        System.out.println("jedis del " + channelId);
        jedis.del(sender + "Netty");
        System.out.println("jedis del " + sender + "Netty");

    }

    @Override
    public synchronized void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 发生异常之后关闭连接（关闭channel），随后从ChannelGroup中移除
        ctx.channel().close();
        users.remove(ctx.channel());
    }
}
