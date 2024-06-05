package com.example.msg.dao;

import com.example.msg.dao.bo.Msg;
import com.example.msg.mapper.jpa.MsgMapper;
import com.example.msg.mapper.po.MsgPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MsgDao {
    private final Logger logger = LoggerFactory.getLogger(MsgDao.class);

    @Autowired
    private MsgMapper msgMapper;

    public void save(Msg msg){
        MsgPo msgPo = new MsgPo(msg);
        System.out.println("save msgPo = " + msgPo);
        logger.debug("save msg: {}", msgPo);
        msgMapper.save(msgPo);
        if(msgPo.getId() == null){
            logger.error("save msg failed: {}", msgPo);
        }
        logger.debug("save msg successfully: {}", msgPo);
    }

    public List<Msg> findByReceiver(String receiver){
        logger.debug("find msg by receiver: {}", receiver);
        List<MsgPo> poList = msgMapper.findByReceiver(receiver);
        if(poList == null){
            logger.debug("no msg found by receiver: {}", receiver);
            return null;
        }
        List<Msg> boList = Msg.toBoList(poList);
        return boList;
    }

    public List<Msg> findByReceiverAndIsSent(String receiver, boolean isSent){
        logger.debug("find msg by receiver: {} and isSent: {}", receiver, isSent);
        List<MsgPo> poList = msgMapper.findByReceiverAndIsSent(receiver, isSent);
        if(poList == null){
            logger.debug("no msg found by receiver: {} and isSent: {}", receiver, isSent);
            return null;
        }
        List<Msg> boList = Msg.toBoList(poList);
        return boList;
    }
}
