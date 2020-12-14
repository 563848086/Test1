package com.offcn.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.offcn.project.contants.ProjectContants;
import com.offcn.project.enums.ProjectImageTypeEnume;
import com.offcn.project.enums.ProjectStatusEnume;
import com.offcn.project.mapper.*;
import com.offcn.project.pojo.*;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Auther: lhq
 * @Date: 2020/9/7 11:36
 * @Description:
 */
@Service
public class ProjectCreateServiceImpl implements ProjectCreateService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private TProjectMapper projectMapper;

    @Autowired
    private TProjectImagesMapper projectImagesMapper;

    @Autowired
    private TProjectTagMapper projectTagMapper;


    @Autowired
    private TProjectTypeMapper projectTypeMapper;

    @Autowired
    private TReturnMapper returnMapper;

    /**
     * 初始化项目
     *
     * @param memberId
     * @return
     */
    @Override
    public String initCreateProject(Integer memberId) {
        //1.创建临时项目对象
        ProjectRedisStorageVo vo = new ProjectRedisStorageVo();
        //2.存放会员ID
        vo.setMemberid(memberId);
        //3.将对象转换成字符串类型
        String jsonStr = JSON.toJSONString(vo);
        //4.讲字符串存入缓存中
        //声明令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        //key  需要拼接令牌常量
        redisTemplate.opsForValue().set(ProjectContants.TEMP_PROJECT_PREFIX + token, jsonStr);
        return token;
    }

    /**
     * 保存项目
     *
     * @param auth           项目状态
     * @param redisStorageVo 项目临时对象
     */
    @Override
    public void saveProjectInfo(ProjectStatusEnume auth, ProjectRedisStorageVo redisStorageVo) {
        //1.保存项目
        TProject project = new TProject();
        //复制对象
        BeanUtils.copyProperties(redisStorageVo, project);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        project.setCreatedate(sdf.format(new Date()));   //创建时间
        project.setStatus(auth.getCode() + "");           //设置项目状态
        projectMapper.insertSelective(project);
        //保存项目成功之后需要得到项目ID 参见Mapper.xml
        Integer projectId = project.getId();

        //2.保存头图
        String headerImage = redisStorageVo.getHeaderImage();
        TProjectImages projectImages = new TProjectImages(null, projectId, headerImage, ProjectImageTypeEnume.HEADER.getCode());
        projectImagesMapper.insertSelective(projectImages);

        //3.保存详情图片
        List<String> imageList = redisStorageVo.getDetailsImage();
        if (!CollectionUtils.isEmpty(imageList)) {
            for (String url : imageList) {
                TProjectImages detailImage = new TProjectImages(null, projectId, url, ProjectImageTypeEnume.DETAILS.getCode());
                projectImagesMapper.insertSelective(detailImage);
            }
        }
        //4.保存项目与标签的关联信息
        List<Integer> tagIds = redisStorageVo.getTagids();
        for (Integer tagId : tagIds) {
            TProjectTag projectTag = new TProjectTag(null, projectId, tagId);
            projectTagMapper.insertSelective(projectTag);
        }

        //5.保存项目与分类的关系信息
        List<Integer> typeIds = redisStorageVo.getTypeids();
        for (Integer typeId : typeIds) {
            TProjectType projectType = new TProjectType(null,projectId,typeId);
            projectTypeMapper.insertSelective(projectType);
        }

        //6.保存回报增量信息
       List<TReturn> returnList =  redisStorageVo.getProjectReturns();
        for(TReturn tReturn:returnList){
            //设置项目ID
            tReturn.setProjectid(projectId);
            returnMapper.insertSelective(tReturn);
        }

        //7.清空Redis
        redisTemplate.delete(ProjectContants.TEMP_PROJECT_PREFIX+redisStorageVo.getProjectToken());

    }
}
