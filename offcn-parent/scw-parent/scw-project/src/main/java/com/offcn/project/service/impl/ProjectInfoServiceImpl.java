package com.offcn.project.service.impl;

import com.offcn.project.mapper.*;
import com.offcn.project.pojo.*;
import com.offcn.project.service.ProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/9/8 09:46
 * @Description:
 */
@Service
public class ProjectInfoServiceImpl implements ProjectInfoService {

    @Autowired
    private TReturnMapper returnMapper;


    @Autowired
    private TProjectMapper projectMapper;


    @Autowired
    private TProjectImagesMapper projectImagesMapper;

    @Autowired
    private TTagMapper tagMapper;


    @Autowired
    private TTypeMapper typeMapper;
    /**
     * 根据项目ID查询回报增量列表
     *
     * @param projectId
     * @return
     */
    @Override
    public List<TReturn> getReturnList(Integer projectId) {
        TReturnExample returnExample = new TReturnExample();
        TReturnExample.Criteria criteria = returnExample.createCriteria();
        //设置项目ID
        criteria.andProjectidEqualTo(projectId);
        return returnMapper.selectByExample(returnExample);
    }

    /**
     * 查询所有项目列表
     *
     * @return
     */
    @Override
    public List<TProject> findAllProject() {
        return projectMapper.selectByExample(null);
    }

    /**
     * 根据项目ID查询关联图片列表
     *
     * @param projectId
     * @return
     */
    @Override
    public List<TProjectImages> findImageList(Integer projectId) {
        TProjectImagesExample imagesExample = new TProjectImagesExample();
        TProjectImagesExample.Criteria criteria = imagesExample.createCriteria();
        criteria.andProjectidEqualTo(projectId);
        return projectImagesMapper.selectByExample(imagesExample);
    }

    /**
     * 根据项目ID获得项目详情
     *
     * @param projectId
     * @return
     */
    @Override
    public TProject findProjectInfo(Integer projectId) {
        return projectMapper.selectByPrimaryKey(projectId);
    }

    /**
     * 查询全部项目标签
     *
     * @return
     */
    @Override
    public List<TTag> findAllProjectTag() {
        return tagMapper.selectByExample(null);
    }

    /**
     * 查询全部项目分类
     *
     * @return
     */
    @Override
    public List<TType> findAllProjectType() {
        return typeMapper.selectByExample(null);
    }

    /**
     * 根据主键查询回报增量详情
     *
     * @param returnId
     * @return
     */
    @Override
    public TReturn findReturn(Integer returnId) {
        return returnMapper.selectByPrimaryKey(returnId);
    }


}
