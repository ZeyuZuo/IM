//package com.example.chat.netty.handler;
//
//import com.example.chat.mapper.jpa.GroupUserMapper;
//import com.example.chat.mapper.po.GroupUserPo;
//import com.example.chat.netty.UserChannel;
//import com.example.chat.netty.element.ChatActionEnum;
//import com.example.chat.netty.element.ChatMsg;
//import com.example.chat.netty.element.Content;
//import com.example.core.auth.TokenUtils;
//import com.example.core.protocol.RequestProtocol;
//import com.example.core.utils.JsonUtils;
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelFutureListener;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.codec.http.*;
//import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
//import io.netty.util.CharsetUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//
//
//@Component
//public class HttpChatHandler extends SimpleChannelInboundHandler<HttpObject>{
//    private final Logger logger = LoggerFactory.getLogger(HttpChatHandler.class);
//
//    @Autowired
//    private GroupUserMapper groupUserMapper;
//
////    @Override
////    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
////        super.channelInactive(ctx);
////    }
////
////    @Override
////    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
////        super.userEventTriggered(ctx, evt);
////    }
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
//        System.out.println("服务端收到消息\n");
//        System.out.println(ctx.channel());
//
//
////        if (msg instanceof HttpRequest) {
////            HttpRequest request = (HttpRequest) msg;
////            System.out.println("http request：\n"+request);
////            System.out.println(formatParams(request));
////        }
////        // responseData.append(RequestUtils.evaluateDecoderResult(request));
////
////        if (msg instanceof HttpContent) {
////            HttpContent httpContent = (HttpContent) msg;
////            System.out.println("http content：\n"+httpContent);
////            System.out.println(formatBody(httpContent));
////            Content content = JsonUtils.json2Obj(formatBody(httpContent), Content.class);
////            System.out.println(content.toString());
////        }
//
//    }
//
//    private StringBuilder formatParams(HttpRequest request) {
//        StringBuilder responseData = new StringBuilder();
//        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
//        Map<String, List<String>> params = queryStringDecoder.parameters();
//        if (!params.isEmpty()) {
//            for (Map.Entry<String, List<String>> p : params.entrySet()) {
//                String key = p.getKey();
//                List<String> vals = p.getValue();
//                for (String val : vals) {
//                    responseData.append("Parameter: ").append(key.toUpperCase()).append(" = ")
//                            .append(val.toUpperCase()).append("\r\n");
//                }
//            }
//            responseData.append("\r\n");
//        }
//        return responseData;
//    }
//
//    private String formatBody(HttpContent httpContent) {
//        StringBuilder responseData = new StringBuilder();
//        ByteBuf content = httpContent.content();
//        if (content.isReadable()) {
//            responseData.append(content.toString(CharsetUtil.UTF_8))
//                    .append("\r\n");
//        }
//        return responseData.toString();
//    }
//
//
//    private void handle(ChannelHandlerContext ctx, Content content){
//        String token = content.getToken();
//        Channel currentChannel = ctx.channel();;
//
//        if(token==null || !TokenUtils.verify(token)){
//            String notVerified = "you're not verified, please login";
//            send(notVerified,currentChannel);
//            return;
//        }
//        Integer action = content.getAction();
//
//        // 2. 判断消息类型，根据不同的类型来处理不同的业务
//        if (Objects.equals(action, ChatActionEnum.CONNECT.getType())) {
//            System.out.println("connecting");
//            ChatMsg chatMsg = content.getChatMsg();
//            if(Objects.equals("server",chatMsg.getReceiver())&&Objects.equals("empty",chatMsg.getGroup())){
//                String sender = chatMsg.getSender();
//                UserChannel.put(sender, currentChannel);
//                UserChannel.output();
//            }else{
//                System.out.println("连接服务器失败");
//            }
//
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
//    }
//
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
//        String group = chatMsg.getGroup();
//
//        if(Objects.equals(group, "empty")){
//            logger.debug("{} send {} to {}",sender,msgText,receiver);
//            System.out.printf("%s send %s to %s\n", sender, msgText, receiver);
//        }else {
//            logger.debug("{} send {} to group {}, this is to {}",sender,msgText,group,receiver);
//            System.out.printf("%s send %s to group %s, this is to %s\n", sender, msgText, group, receiver);
//        }
//
//        // Channel receiverChannel = UserChannel.get(receiver);
//        receiverChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.obj2Json(chatMsg)));
//    }
//
//    private void send(Object s, Channel receiverChannel){
//        if(receiverChannel==null){
//            logger.debug("receiver offline");
//            System.out.println("receiver offline");
//            return;
//        }
//
//        receiverChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.obj2Json(s)));
//    }
//}
