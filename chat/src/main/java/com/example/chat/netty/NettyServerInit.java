//package com.example.chat.netty;
//
//import com.example.chat.netty.handler.ChatHandler;
//// import com.example.chat.netty.handler.HttpChatHandler;
//import com.example.chat.netty.handler.HttpChatHandler;
//import com.example.core.protocol.RequestProtocol;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.handler.codec.http.HttpResponseEncoder;
//import io.netty.handler.codec.http.HttpServerCodec;
//import io.netty.handler.codec.protobuf.ProtobufDecoder;
//import io.netty.handler.codec.protobuf.ProtobufEncoder;
//import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
//import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
//import io.netty.handler.timeout.IdleStateHandler;
//
//public class NettyServerInit extends ChannelInitializer<SocketChannel> {
//
//    @Override
//    protected void initChannel(SocketChannel socketChannel) throws Exception{
//        socketChannel.pipeline()
//                .addLast(new IdleStateHandler(100, 0, 0))
//                .addLast(new HttpServerCodec())
//                .addLast(new HttpResponseEncoder())
////                .addLast(new ProtobufVarint32FrameDecoder())
////                .addLast(new ProtobufDecoder(RequestProtocol.ReqProtocol.getDefaultInstance()))
////                .addLast(new ProtobufVarint32LengthFieldPrepender())
////                .addLast(new ProtobufEncoder())
//                .addLast(new HttpChatHandler());
//    }
//}
