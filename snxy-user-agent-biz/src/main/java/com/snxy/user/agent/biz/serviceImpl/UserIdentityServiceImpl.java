package com.snxy.user.agent.biz.serviceImpl;

import com.snxy.user.agent.dao.mapper.UserIdentityMapper;
import com.snxy.user.agent.domain.IdentityType;
import com.snxy.user.agent.domain.OnlineUser;
import com.snxy.user.agent.domain.UserIdentity;
import com.snxy.user.agent.service.IdentityTypeService;
import com.snxy.user.agent.service.OnlineUserService;
import com.snxy.user.agent.service.UserIdentityService;
import com.snxy.user.agent.service.vo.UserIdentityVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 24398 on 2018/8/31.
 */

@Service
@Slf4j
public class UserIdentityServiceImpl implements UserIdentityService {

    @Resource
    private UserIdentityMapper userIdentityMapper;
    @Resource
    private IdentityTypeService  identityTypeService;
    @Resource
    private OnlineUserService onlineUserService;

    @Override
    public List<UserIdentityVO> getIdentityBySystemUserId(Long systemUserId) {
        OnlineUser onlineUser = this.onlineUserService.getBySystemUserId(systemUserId,false);
        List<UserIdentity> userIdentities = this.userIdentityMapper.getByOnlineUserId(onlineUser.getId(),false);
        if(userIdentities == null || userIdentities.isEmpty()){
            return Collections.emptyList();
        }
        // 查询IdentityType 集合
        List<Integer> identityTypeIds = userIdentities.parallelStream().map(UserIdentity::getIdentityId).collect(Collectors.toList());
        List<IdentityType> identityTypes = this.identityTypeService.getByIds(identityTypeIds,false);
        // 转换
        List<UserIdentityVO> userIdentityVOS = identityTypes.parallelStream().map(identityType -> UserIdentityVO.builder()
                                                                    .identityTypeId(identityType.getId())
                                                                    .identityName(identityType.getIdentityName())
                                                                    .build())
                                                                    .collect(Collectors.toList());

        return userIdentityVOS;
    }


    @Override
    public void insert(UserIdentity userIdentity) {
        this.userIdentityMapper.insertSelective(userIdentity);
    }
}
