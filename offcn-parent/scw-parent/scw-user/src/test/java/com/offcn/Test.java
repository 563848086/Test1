package com.offcn;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Auther: lhq
 * @Date: 2020/9/6 11:25
 * @Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ScwUserStart.class})
@Slf4j   //private Logger logger = new Logger(Test.class);
public class Test {


    //@Autowired
    //private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @org.junit.Test
    public void setValue(){
        stringRedisTemplate.opsForValue().set("message","欢迎来到优就业学习");
    }
}
