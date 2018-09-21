package com.snxy.user.agent.biz.serviceImpl;

import com.snxy.user.agent.dao.mapper.StaffMapper;
import com.snxy.user.agent.domain.Staff;
import com.snxy.user.agent.service.StaffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class StaffServiceImpl implements StaffService {

    @Resource
    private StaffMapper staffMapper;

    @Override
    public Staff selectOne(Long systemUserId) {
        Staff staff = this.staffMapper.selectBySystemUserId(systemUserId);
        return staff;
    }
}
