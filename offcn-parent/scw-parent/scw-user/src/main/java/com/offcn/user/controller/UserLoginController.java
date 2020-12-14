package com.offcn.user.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.user.compent.SmsUtil;
import com.offcn.user.pojo.TMember;
import com.offcn.user.service.UserService;
import com.offcn.user.vo.resp.UserRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: lhq
 * @Date: 2020/9/6 14:49
 * @Description: 用户登录控制层
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户登录Controller")
public class UserLoginController {

    @Autowired
    private SmsUtil smsUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;


    @PostMapping("/sendCode")
    @ApiOperation(value = "发送短信验证码")
    @ApiImplicitParam(name = "phoneNo", value = "手机号", required = true)
    public AppResponse<Object> sendCode(String phoneNo) {
        //1.生成随机数
        String code = UUID.randomUUID().toString().substring(0, 4);
        //2.将随机数保存到缓存中  ,设置五分钟后超时 超时清除缓存信息
        redisTemplate.opsForValue().set(phoneNo, code, 60 * 5, TimeUnit.SECONDS);
        //3.给手机发送验证码
        try {
            smsUtil.sendSms(phoneNo, code);
            return AppResponse.ok("短信发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.fail("短信发送失败");
        }
    }


    @GetMapping("/login")
    @ApiOperation(value = "用户登录")
    @ApiImplicitParams({@ApiImplicitParam(name = "username", value = "账号", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true)})
    public AppResponse<Object> login(String username, String password) {
        //1.验证用户是否存在，登录成功
        TMember member = userService.login(username, password);
        if (null != member) {
            //2.如果登陆成功，则发放令牌
            String accessToken = UUID.randomUUID().toString().replace("-", "");
            //复制对象
            UserRespVo userRespVo = new UserRespVo();
            BeanUtils.copyProperties(member, userRespVo);
            userRespVo.setAccessToken(accessToken); //设置令牌
            //3.将令牌及用户ID放入缓存，超时时间设置为两小时
            redisTemplate.opsForValue().set(accessToken, member.getId() + "", 2, TimeUnit.HOURS);
            return AppResponse.ok(userRespVo);
        } else {
            return AppResponse.fail("登录失败，用户名或密码不匹配");
        }
    }


}
