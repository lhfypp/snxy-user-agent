package com.snxy.user.agent.dao;

import com.snxy.user.agent.dao.mapper.SystemUserMapper;
import com.snxy.user.agent.domain.SystemUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
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

	@Test
	public void getByAccount(){
       SystemUser systemUser1 = systemUserMappe.getByAccount("123");

       SystemUser systemUser2 = systemUserMappe.getByAccount("100002");

       log.info(" systemUser1 ： [{}]",systemUser1 );

       log.info("systemUser2 : [{}]",systemUser2);


	}

}
