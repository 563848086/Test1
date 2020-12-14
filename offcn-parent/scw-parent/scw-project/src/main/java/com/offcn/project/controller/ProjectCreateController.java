package com.offcn.project.controller;

import com.alibaba.fastjson.JSON;
import com.offcn.dycommon.response.AppResponse;
import com.offcn.project.contants.ProjectContants;
import com.offcn.project.enums.ProjectStatusEnume;
import com.offcn.project.pojo.TReturn;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.req.ProjectBaseInfoVo;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import com.offcn.project.vo.req.ProjectReturnVo;
import com.offcn.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/9/7 11:44
 * @Description: 创建项目的控制器
 */
@RestController
@RequestMapping("/projct")
@Api(tags = "创建项目")
@Slf4j
public class ProjectCreateController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProjectCreateService projectCreateService;


    @ApiOperation("发起项目第一步--创建项目，阅读并同意协议")
    @PostMapping("/init")
    public AppResponse<Object> init(BaseVo baseVo) {
        //1.获得用户登录的令牌
        String accessToken = baseVo.getAccessToken();
        //2.根据令牌在缓存中获得会员ID
        String memberId = redisTemplate.opsForValue().get(accessToken);
        if (memberId == null) {
            return AppResponse.fail("无权限操作，请先登录");
        }
        //3.根据ID，调用初始化项目的方法
        String projectToken = projectCreateService.initCreateProject(Integer.parseInt(memberId));
        return AppResponse.ok(projectToken);

    }

    @ApiOperation("发起项目第二步--收集表单项目基本信息")
    @PostMapping("/saveBaseInfo")
    public AppResponse<Object> saveBaseInfo(@RequestBody ProjectBaseInfoVo baseInfoVo) {
        //1.根据项目令牌从缓存中取得临时项目对象
        String projectContext = redisTemplate.opsForValue().get(ProjectContants.TEMP_PROJECT_PREFIX + baseInfoVo.getProjectToken());
        //2.需要JSON类型的字符串转换成对象格式
        ProjectRedisStorageVo redisStorageVo = JSON.parseObject(projectContext, ProjectRedisStorageVo.class);
        //3.复制对象 baseInfoVo--》redisStorageVo
        BeanUtils.copyProperties(baseInfoVo, redisStorageVo);
        //4.放回到Redis中
        String jsonStr = JSON.toJSONString(redisStorageVo);
        redisTemplate.opsForValue().set(ProjectContants.TEMP_PROJECT_PREFIX + baseInfoVo.getProjectToken(), jsonStr);
        return AppResponse.ok("收集页面项目信息成功");

    }

    @ApiOperation("发起项目第三步--收集项目回报增量信息")
    @PostMapping("/saveReturnInfo")
    public AppResponse<Object> saveReturnInfo(@RequestBody List<ProjectReturnVo> returnVoList) {
        //1.获得项目令牌，从缓存中的取得临时对象
        String projectToken = returnVoList.get(0).getProjectToken();
        String projectContext = redisTemplate.opsForValue().get(ProjectContants.TEMP_PROJECT_PREFIX + projectToken);
        //2.将字符串转换成临时对象
        ProjectRedisStorageVo redisStorageVo = JSON.parseObject(projectContext, ProjectRedisStorageVo.class);
        //3.遍历表单VO  得到回报信息对象
        List<TReturn> returnList = new ArrayList<>();
        for (ProjectReturnVo returnVo : returnVoList) {
            TReturn tReturn = new TReturn();
            //4.复制对象  returnVo--->tReturn
            BeanUtils.copyProperties(returnVo, tReturn);
            //5.将POJO放入到集合中
            returnList.add(tReturn);
        }
        //6.将回报信息的集合设置回临时对象
        redisStorageVo.setProjectReturns(returnList);
        //7.转换类型，将临时对象放回到缓存中
        String jsonStr = JSON.toJSONString(redisStorageVo);
        redisTemplate.opsForValue().set(ProjectContants.TEMP_PROJECT_PREFIX + projectToken, jsonStr);
        return AppResponse.ok("收集项目回报增量信息成功");

    }


    @ApiOperation("发起项目第四步--保存项目信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "accessToken",value = "登录令牌",required = true)
            ,@ApiImplicitParam(name = "projectToken",value = "项目令牌",required = true),
            @ApiImplicitParam(name = "ops",value = "用户操作类型 0-保存草稿 1-提交审核",required = true)})
    @PostMapping("/saveProjectInfo")
    public AppResponse saveProjectInfo(String accessToken, String projectToken, String ops) {
        //1.通过登录令牌，获得会员ID
        String memberId = redisTemplate.opsForValue().get(accessToken);
        if (memberId == null) {
            return AppResponse.fail("无此权限，请先登录");
        }
        //2.通过项目令牌，获得临时对象,并做类型转换
        String projectContext = redisTemplate.opsForValue().get(ProjectContants.TEMP_PROJECT_PREFIX + projectToken);
        ProjectRedisStorageVo redisStorageVo = JSON.parseObject(projectContext, ProjectRedisStorageVo.class);
        //3.判断OPS操作类型是否为空，并做相应处理
        if (StringUtils.isNotEmpty(ops)) {
            //保存项目，提交审核
            if (ops.equals("1")) {
                projectCreateService.saveProjectInfo(ProjectStatusEnume.SUBMIT_AUTH, redisStorageVo);
                return AppResponse.ok("保存项目成功，并提交审核");
            } else if (ops.equals("0")) {    //保存草稿状态
                projectCreateService.saveProjectInfo(ProjectStatusEnume.DRAFT, redisStorageVo);
                return AppResponse.ok("项目保存到草稿状态");
            } else {
                return AppResponse.fail("不支持此操作");
            }
        }
        return AppResponse.fail(null);

    }


}
