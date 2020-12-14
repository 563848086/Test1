package com.offcn.user.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.user.pojo.TMember;
import com.offcn.user.service.UserService;
import com.offcn.user.vo.req.UserRegisterVo;
import com.offcn.user.vo.resp.UserRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import com.offcn.user.bean.User;

/**
 * @Auther: lhq
 * @Date: 2020/9/6 11:49
 * @Description:
 */
@RestController
@RequestMapping("/user")
@Api(tags = "第一个Swagger测试")
public class UserController {


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @GetMapping("/getUser")
    @ApiOperation(value = "根据名称获得用户")
    @ApiImplicitParam(name = "name", value = "姓名", required = true)
    public User getUser(String name) {
        User user = new User(1L, name, 25);
        return user;
    }

    @ApiOperation(value = "用户注册")
    @ApiImplicitParam(name="userRegisterVo",value = "用户信息",required = true,dataType = "UserRegisterVo")
    @PostMapping("/register")
    public AppResponse<Object> register(@RequestBody UserRegisterVo userRegisterVo){

        String code =   redisTemplate.opsForValue().get(userRegisterVo.getLoginacct());
        if(StringUtils.isNotEmpty(code)){
            //1.判断短信验证码是否正确
            if(code.equals(userRegisterVo.getCode())){
                //2.保存用户
                //复制对象
                TMember member = new TMember();
                BeanUtils.copyProperties(userRegisterVo,member);   //注意：复制的两个类中的字段声明名称要一致
                try {
                    userService.register(member);
                    //3.如果保存成功，清空缓存中的验证码
                    redisTemplate.delete(userRegisterVo.getLoginacct());
                    return AppResponse.ok("注册成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    return AppResponse.fail("注册失败");
                }
            }else{
                return AppResponse.fail("验证码不正确");
            }

        }else{
            return  AppResponse.fail("验证码不存在,请重新发送");
        }

    }


    @GetMapping("/findTMemberById")
    @ApiOperation(value = "查询会员信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "id",value = "主键",required = true)})
    public AppResponse<Object> findTMemberById(Integer id){
        TMember member = userService.findTMemberById(id);
        UserRespVo vo = new UserRespVo();
        BeanUtils.copyProperties(member,vo);
        return AppResponse.ok(vo);
    }






}
