package com.example.user.mapper.jpa;

import com.example.user.mapper.po.LogInPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LogInMapper extends JpaRepository<LogInPo, Long> {
    LogInPo findByUser(String User);
}
