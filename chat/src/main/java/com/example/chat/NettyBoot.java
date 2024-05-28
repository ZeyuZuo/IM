package com.example.chat;

import com.example.chat.mapper.jpa.GroupUserMapper;
import com.example.chat.netty.WSServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class NettyBoot implements ApplicationListener<ContextRefreshedEvent> {

    private GroupUserMapper groupUserMapper;

    @Autowired
    public NettyBoot(GroupUserMapper groupUserMapper){
        this.groupUserMapper = groupUserMapper;
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent()==null) {
            try {
                new WSServer(groupUserMapper).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
