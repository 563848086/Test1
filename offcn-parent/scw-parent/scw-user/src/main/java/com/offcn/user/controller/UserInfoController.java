package com.offcn.user.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.user.pojo.TMemberAddress;
import com.offcn.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/9/8 11:15
 * @Description:
 */
@RestController
@RequestMapping("/user")
@Api(tags = "查询会员相关信息")
@Slf4j
public class UserInfoController {


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @ApiOperation("根据会员ID查询地址列表")
    @ApiImplicitParam(name="accessToken",value="登录令牌",required = true)
    @GetMapping("/info/address")
    public AppResponse<Object> findAddressList(String accessToken) {
        //1.通过登录令牌得到会员ID
        String memberId = redisTemplate.opsForValue().get(accessToken);
        if (memberId == null) {
            return AppResponse.fail("无此权限，请先登录");
        }
        //2.根据会员ID查询地址列表
        List<TMemberAddress> memberAddressList = userService.findAddressList(Integer.parseInt(memberId));
        return AppResponse.ok(memberAddressList);
    }
}
