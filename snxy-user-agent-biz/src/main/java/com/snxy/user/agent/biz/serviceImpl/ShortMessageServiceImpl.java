package com.snxy.user.agent.biz.serviceImpl;

import com.snxy.user.agent.dao.mapper.ShortMessageMapper;
import com.snxy.user.agent.domain.ShortMessage;
import com.snxy.user.agent.service.ShortMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by 24398 on 2018/11/5.
 */
@Service
@Slf4j
public class ShortMessageServiceImpl implements ShortMessageService{

    @Resource
    private ShortMessageMapper shortMessageMapper;

    @Override
    public void saveShortMessage(ShortMessage shortMessage) {
        this.shortMessageMapper.insertSelective(shortMessage);
    }
}
