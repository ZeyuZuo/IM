package com.example.chat.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.example.chat.ChatApplication;
import com.example.chat.mapper.jpa.GroupUserMapper;
import com.example.chat.mapper.po.GroupUserPo;
import com.example.chat.netty.UserChannel;
import com.example.chat.netty.element.ChatActionEnum;
import com.example.chat.netty.element.ChatMsg;
import com.example.chat.netty.element.Content;
import com.example.chat.netty.utils.JsonUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;


@Component
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private GroupUserMapper groupUserMapper;

    @Autowired
    public ChatHandler(GroupUserMapper groupUserMapper){
        this.groupUserMapper = groupUserMapper;
    }

    private void send(ChatMsg chatMsg){
        String sender = chatMsg.getSender();
        String receiver = chatMsg.getReceiver();
        String msgText = chatMsg.getMsg();
        String group = chatMsg.getGroup();

        if(Objects.equals(group, "empty")){
            System.out.printf("%s send %s to %s\n", sender, msgText, receiver);
        }else {
            System.out.printf("%s send %s to group %s, this is to %s\n", sender, msgText, group, receiver);
        }

        Channel receiverChannel = UserChannel.get(receiver);
        if (receiverChannel == null) {
            System.out.println("receiver offline");
        } else {
            Channel findChannel = users.find(receiverChannel.id());
            if (findChannel != null) {
                receiverChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.obj2Json(chatMsg)));
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        // 获取客户端传输过来的消息
        String text = msg.text();
        Channel currentChannel = ctx.channel();

        // 1. 获取客户端发来的消息
        Content content = JsonUtils.json2Obj(text, Content.class);
        Integer action = content.getAction();


        // 2. 判断消息类型，根据不同的类型来处理不同的业务
        if (Objects.equals(action, ChatActionEnum.CONNECT.getType())) {
            System.out.println("connecting");
            ChatMsg chatMsg = content.getChatMsg();
            if(Objects.equals("server",chatMsg.getReceiver())&&Objects.equals("empty",chatMsg.getGroup())){
                String sender = chatMsg.getSender();
                UserChannel.put(sender, currentChannel);
                for (Channel channel : users) {
                    System.out.println(channel.id().asLongText());
                }
                UserChannel.output();
            }else{
                System.out.println("连接服务器失败");
            }

        } else if (Objects.equals(action, ChatActionEnum.CHAT.getType())) {
            ChatMsg chatMsg = content.getChatMsg();
            if(Objects.equals("empty",chatMsg.getGroup())){
                send(chatMsg);
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
                if(!Objects.equals(receiver,sender)){
                    chatMsg.setReceiver(receiver);
                    send(chatMsg);
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
