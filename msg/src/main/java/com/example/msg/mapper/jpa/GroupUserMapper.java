package com.example.msg.mapper.jpa;


import com.example.msg.mapper.po.GroupUserPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupUserMapper extends JpaRepository<GroupUserPo, Long> {
    List<GroupUserPo> findByGroup(String group);
}
