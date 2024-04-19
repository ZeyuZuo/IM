package com.example.chat.mapper.jpa;


import com.example.chat.mapper.po.GroupUserPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
public interface GroupUserMapper extends JpaRepository<GroupUserPo, Long> {
    List<GroupUserPo> findByGroup(String group);
}
