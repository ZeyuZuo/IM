package com.example.core.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class IpUtils {
    public static String getPrivateIp() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    String ipAddress = address.getHostAddress();
                    if (isPrivateAddress(ipAddress)) {
                        return ipAddress;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public String getPublicIp(){
//        try{
//            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
//            while (interfaces.hasMoreElements()) {
//                NetworkInterface networkInterface = interfaces.nextElement();
//                if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
//                    continue;
//                }
//
//                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
//                while (addresses.hasMoreElements()) {
//                    InetAddress address = addresses.nextElement();
//                    String ipAddress = address.getHostAddress();
//                    if (!isPrivateAddress(ipAddress)) {
//                        return ipAddress;
//                    }
//                }
//            }
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//    }

    private static boolean isPrivateAddress(String ipAddress) {
        // 判断IP地址是否在私有地址范围内
        return ipAddress.startsWith("192.168.");
    }
}
