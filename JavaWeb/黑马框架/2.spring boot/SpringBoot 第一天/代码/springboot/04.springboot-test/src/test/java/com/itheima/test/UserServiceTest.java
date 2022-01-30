package com.itheima.test;

import com.itheima.springboottest.SpringbootTestApplication;
import com.itheima.springboottest.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * userService的测试类
 * @RunWith表示这是一个测试类
 * @SpringBootTest指定要引入的配置，
 * 因为这个测试类的目录结构和启动类不一样，所以才需要指定classes。
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootTestApplication.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testAdd() {
        userService.add();
    }
}
