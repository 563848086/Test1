package com.offcn.project.service;

import com.offcn.project.enums.ProjectStatusEnume;
import com.offcn.project.vo.req.ProjectRedisStorageVo;

/**
 * @Auther: lhq
 * @Date: 2020/9/7 11:35
 * @Description:  创建项目的接口
 */
public interface ProjectCreateService {


    /**
     * 初始化项目
     * @param memberId
     * @return
     */
    public String  initCreateProject(Integer memberId);

    /**
     * 保存项目
     * @param auth  项目状态
     * @param redisStorageVo  项目临时对象
     */
    public void saveProjectInfo(ProjectStatusEnume auth, ProjectRedisStorageVo redisStorageVo);
}
