package com.snxy.user.agent.dao;

import com.snxy.user.agent.dao.mapper.SystemUserMapper;
import com.snxy.user.agent.domain.SystemUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SnxyUserAgentDaoApplicationTests {

	@Resource
	private SystemUserMapper systemUserMappe;

	@Test
	public void selectByPrimaryKeyTest() {
		SystemUser systemUser=systemUserMappe.selectByPrimaryKey(1L);
		Assert.assertTrue("不应该为空值",systemUser!=null);
       log.info(systemUser.toString());
	}

}
