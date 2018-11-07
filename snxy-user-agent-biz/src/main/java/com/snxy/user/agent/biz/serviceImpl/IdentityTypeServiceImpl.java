package com.snxy.user.agent.biz.serviceImpl;

import com.snxy.user.agent.dao.mapper.IdentityTypeMapper;
import com.snxy.user.agent.domain.IdentityType;
import com.snxy.user.agent.service.IdentityTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * Created by 24398 on 2018/8/31.
 */
@Service
@Slf4j
public class IdentityTypeServiceImpl implements IdentityTypeService {

    @Resource
    private IdentityTypeMapper identityTypeMapper;


    @Override
    public List<IdentityType> getByIds(List<Integer> identityIds, Boolean isDelete) {
        if(identityIds == null || identityIds.isEmpty()){
            return Collections.emptyList();
        }
        List<IdentityType> identityTypes = this.identityTypeMapper.selectByIds(identityIds,false);

        return identityTypes;
    }
}
