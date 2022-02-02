package org.example;

import lombok.extern.slf4j.Slf4j;

import java.sql.Date;

/**
 * @Author: ShenGuopin
 * @Date: 2022/2/2 21:09
 * @Version: 1.0
 */
@Slf4j
public class Client {
    public static void main(String[] args) {
        User user = new User();
        user.setBirthday(new Date(System.currentTimeMillis()));
        user.setName("小明");
        System.out.println(user);

        User user1 = new User.UserBuilder().id(2).name("老王").build();
        System.out.println(user1);

        //private Logger log = LoggerFactory.getLogger(this.getClass());
    }
}
