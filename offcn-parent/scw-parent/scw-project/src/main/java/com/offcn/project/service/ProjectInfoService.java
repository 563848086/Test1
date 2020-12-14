package com.offcn.project.service;

import com.offcn.project.pojo.*;

import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/9/8 09:45
 * @Description:  项目信息接口
 */
public interface ProjectInfoService {

    /**
     * 根据项目ID查询回报增量列表
     * @param projectId
     * @return
     */
    public List<TReturn> getReturnList(Integer projectId);

    /**
     * 查询所有项目列表
     * @return
     */
    public List<TProject> findAllProject();


    /**
     * 根据项目ID查询关联图片列表
     * @param projectId
     * @return
     */
    public List<TProjectImages> findImageList(Integer projectId);

    /**
     * 根据项目ID获得项目详情
     * @param projectId
     * @return
     */
    public TProject findProjectInfo(Integer projectId);

    /**
     * 查询全部项目标签
     * @return
     */
    public List<TTag> findAllProjectTag();


    /**
     * 查询全部项目分类
     * @return
     */
    public List<TType> findAllProjectType();


    /**
     * 根据主键查询回报增量详情
     * @param returnId
     * @return
     */
    public TReturn findReturn(Integer returnId);

}
