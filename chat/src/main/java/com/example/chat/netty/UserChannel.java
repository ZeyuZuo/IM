//package com.example.chat.netty;
//
//import io.netty.channel.Channel;
//
//import java.util.HashMap;
//
//public class UserChannel {
//    private static HashMap<String, Channel> manager = new HashMap<>();
//
//    public static void put(String sender, Channel channel) {
//        manager.put(sender, channel);
//    }
//
//    public static Channel get(String sender) {
//        return manager.get(sender);
//    }
//
//    public static void output() {
//        for (HashMap.Entry<String, Channel> entry : manager.entrySet()) {
//            System.out.println("User: " + entry.getKey()
//                    + ", ChannelId: " + entry.getValue().id().asLongText());
//        }
//    }
//}
