//package com.example.chat.netty.handler;
//
//import com.example.core.protocol.RequestProtocol;
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelFutureListener;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.codec.http.*;
//import io.netty.util.CharsetUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.List;
//import java.util.Map;
//
//public class HttpChatHandler extends SimpleChannelInboundHandler{
//    private final static Logger LOGGER = LoggerFactory.getLogger(ChatHandler.class);
//
//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        super.channelInactive(ctx);
//    }
//
//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        super.userEventTriggered(ctx, evt);
//    }
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("服务端收到消息\n");
//        System.out.println(ctx.channel());
//        if (msg instanceof HttpRequest) {
//            HttpRequest request = (HttpRequest) msg;
//            System.out.println("http request：\n"+request);
//            System.out.println(formatParams(request));
//        }
//        // responseData.append(RequestUtils.evaluateDecoderResult(request));
//
//        if (msg instanceof HttpContent) {
//            HttpContent httpContent = (HttpContent) msg;
//            System.out.println("http content：\n"+httpContent);
//            System.out.println(formatBody(httpContent));
//        }
//    }
//
//    StringBuilder formatParams(HttpRequest request) {
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
//    StringBuilder formatBody(HttpContent httpContent) {
//        StringBuilder responseData = new StringBuilder();
//        ByteBuf content = httpContent.content();
//        if (content.isReadable()) {
//            responseData.append(content.toString(CharsetUtil.UTF_8))
//                    .append("\r\n");
//        }
//        return responseData;
//    }
//}
