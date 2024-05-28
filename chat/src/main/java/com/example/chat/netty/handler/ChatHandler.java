package com.example.chat.netty.handler;

import com.example.chat.mapper.jpa.GroupUserMapper;
import com.example.chat.mapper.po.GroupUserPo;
import com.example.core.auth.TokenUtils;
import com.example.core.element.ChatActionEnum;
import com.example.core.element.ChatMsg;
import com.example.core.element.Content;
import com.example.core.utils.IpUtils;
import com.example.core.utils.JsonUtils;
import com.example.core.utils.RedisUtils;
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

import java.net.Inet4Address;
import java.util.List;
import java.util.Objects;


@Component
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private final Logger logger = LoggerFactory.getLogger(ChatHandler.class);

    private GroupUserMapper groupUserMapper;
    private RedisUtils redisUtils;

    @Autowired
    public ChatHandler(GroupUserMapper groupUserMapper){
        this.groupUserMapper = groupUserMapper;
    }


    private void sendChatMsg(ChatMsg chatMsg, Channel receiverChannel){

        if(receiverChannel == null){
            logger.debug("receiver {} offline",chatMsg.getReceiver());
            System.out.println("receiver offline");
            return;
        }

        String sender = chatMsg.getSender();
        String receiver = chatMsg.getReceiver();
        String msgText = chatMsg.getMsg();
        String group = chatMsg.getGroup();

        if(Objects.equals(group, "empty")){
            logger.debug("{} send {} to {}",sender,msgText,receiver);
            System.out.printf("%s send %s to %s\n", sender, msgText, receiver);
        }else {
            logger.debug("{} send {} to group {}, this is to {}",sender,msgText,group,receiver);
            System.out.printf("%s send %s to group %s, this is to %s\n", sender, msgText, group, receiver);
        }

        // Channel receiverChannel = UserChannel.get(receiver);
        if (receiverChannel == null) {
            System.out.println("receiver offline");
        } else {
            Channel findChannel = users.find(receiverChannel.id());
            if (findChannel != null) {
                receiverChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.obj2Json(chatMsg)));
            }
        }
    }

    private void send(Object s, Channel receiverChannel){
        if(receiverChannel==null){
            logger.debug("receiver offline");
            System.out.println("receiver offline");
        }

        Channel findChannel = users.find(receiverChannel.id());
        if (findChannel != null) {
            receiverChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.obj2Json(s)));
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        String text = msg.text();
        Channel currentChannel = ctx.channel();


        // 1. 获取客户端发来的消息
        Content content = new Content();
        try{
            content = JsonUtils.json2Obj(text, Content.class);
        }catch(Exception e){
            logger.debug("消息转换失败");
            System.out.println("消息转换失败");
        }
        String token = content.getToken();

        if(token==null || !TokenUtils.verify(token)){
            String notVerified = "you're not verified, please login";
            send(notVerified,currentChannel);
            return;
        }
        Integer action = content.getAction();

        // 2. 判断消息类型，根据不同的类型来处理不同的业务
        if (Objects.equals(action, ChatActionEnum.CONNECT.getType())) {
            System.out.println("connecting");
            ChatMsg chatMsg = content.getChatMsg();
            if(Objects.equals("server",chatMsg.getReceiver())&&Objects.equals("empty",chatMsg.getGroup())){
                String sender = chatMsg.getSender();
                // UserChannel.put(sender, currentChannel);
                for (Channel channel : users) {
                    System.out.println(channel.id().asLongText());
                }
                // UserChannel.output();
                redisUtils.set(chatMsg.getSender(), IpUtils.getIp());
                System.out.println(redisUtils.get(chatMsg.getSender()));
            }else{
                System.out.println("连接服务器失败");
            }

        } else if (Objects.equals(action, ChatActionEnum.CHAT.getType())) {
            ChatMsg chatMsg = content.getChatMsg();
            // Channel receiverChannel = UserChannel.get(content.getChatMsg().getReceiver());
            if(Objects.equals("empty",chatMsg.getGroup())){
                sendChatMsg(chatMsg, receiverChannel);
            }
        } else if(Objects.equals(action, ChatActionEnum.GROUPCHAT.getType())) {
            ChatMsg chatMsg = content.getChatMsg();
            String sender = chatMsg.getSender();
            String group = chatMsg.getGroup();
            System.out.println(group);

            List<GroupUserPo> poList = groupUserMapper.findByGroup(group);

            for(GroupUserPo po : poList){
                // System.out.println(po.getUser());
                String receiver = po.getUser();
                Channel receiverChannel = UserChannel.get(receiver);
                if(!Objects.equals(receiver,sender)){
                    chatMsg.setReceiver(receiver);
                    sendChatMsg(chatMsg, receiverChannel);
                }
            }
        }
    }

        /**
         * 当客户端连接服务端之后（打开连接）
         * 获取客户端的channle，并且放到ChannelGroup中去进行管理
         */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("add a client");
        users.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

        String channelId = ctx.channel().id().asShortText();
        System.out.println("客户端被移除，channelId为：" + channelId);

        // 当触发handlerRemoved，ChannelGroup会自动移除对应客户端的channel
        users.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 发生异常之后关闭连接（关闭channel），随后从ChannelGroup中移除
        ctx.channel().close();
        users.remove(ctx.channel());
    }
}
