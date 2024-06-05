package com.example.msg.mapper.jpa;

import com.example.msg.mapper.po.MsgPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MsgMapper extends JpaRepository<MsgPo, Long> {
    List<MsgPo> findByReceiver(String receiver);
    List<MsgPo> findByReceiverAndIsSent(String receiver, boolean isSent);
}
