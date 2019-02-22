package com.atguigu.mybatis_plus_180925;

import com.atguigu.mybatis_plus_180925.entity.User;
import com.atguigu.mybatis_plus_180925.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisPlus180925ApplicationTests {
	@Autowired
	private UserMapper userMapper;
	@Test
	public void contextLoads() {
		System.out.println(("----- selectAll method test ------"));
		//UserMapper 中的 selectList() 方法的参数为 MP 内置的条件封装器 Wrapper
		//所以不填写就是无任何条件
		List<User> users = userMapper.selectList(null);
		users.forEach(System.out::println);

	}


}
