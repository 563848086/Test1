package com.offcn.project.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.project.pojo.*;
import com.offcn.project.service.ProjectInfoService;
import com.offcn.project.vo.resp.ProjectDetailVo;
import com.offcn.project.vo.resp.ProjectVo;
import com.offcn.utils.OSSTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: lhq
 * @Date: 2020/9/7 10:36
 * @Description: 创建项目、上传文件的控制器
 */
@RestController
@RequestMapping("/project")
@Api(tags = "项目信息（创建项目、上传文件）")
@Slf4j
public class ProjectInfoController {


    @Autowired
    private OSSTemplate ossTemplate;

    @Autowired
    private ProjectInfoService projectInfoService;

    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public AppResponse<Object> upload(@RequestParam(name = "file") MultipartFile[] multipartFiles) {
        //存放文件地址列表的集合
        List<String> list = new ArrayList<String>();
        Map map = new HashMap();
        if (multipartFiles != null && multipartFiles.length > 0) {
            for (MultipartFile file : multipartFiles) {
                if (file != null) {
                    try {
                        String url = ossTemplate.upload(file.getInputStream(), file.getOriginalFilename());
                        list.add(url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        map.put("urls", list);
        return AppResponse.ok(map);
    }


    @ApiOperation("查询回报增量列表")
    @GetMapping("/details/getReturnList/{projectId}")
    public AppResponse<List<TReturn>> getReturnList(@PathVariable(name = "projectId") Integer projectId) {
        List<TReturn> returnList = projectInfoService.getReturnList(projectId);
        return AppResponse.ok(returnList);
    }


    @ApiOperation("查询所有项目列表")
    @GetMapping("/findAll")
    public AppResponse<Object> findAll() {
        //1.查询全部项目
        List<TProject> projectList = projectInfoService.findAllProject();
        //重新将Vo添加到集合中
        List<ProjectVo> projectVoList = new ArrayList<>();
        for (TProject project : projectList) {
            //2.根据项目ID查询图片集合
            List<TProjectImages> imagesList = projectInfoService.findImageList(project.getId());
            //3.复制对象  project----》vo
            ProjectVo projectVo = new ProjectVo();
            BeanUtils.copyProperties(project, projectVo);

            if (!CollectionUtils.isEmpty(imagesList)) {
                for (TProjectImages image : imagesList) {
                    //选择头图
                    if (image.getImgtype() == 0) {
                        projectVo.setHeaderImage(image.getImgurl());
                    }
                }
            }
            projectVoList.add(projectVo);

        }

        return AppResponse.ok(projectVoList);
    }

    @ApiOperation("获得项目详情")
    @ApiImplicitParam(name = "projectId",value = "项目主键",required = true)
    @GetMapping("/findProjectInfo")
    public AppResponse<Object> findProjectInfo(Integer projectId) {
        //1.查询项目详情
        TProject project = projectInfoService.findProjectInfo(projectId);
        //2.复制对象  project---》projectDetailVo
        ProjectDetailVo projectDetailVo = new ProjectDetailVo();
        BeanUtils.copyProperties(project, projectDetailVo);
        //3.获得项目的所有图片
        List<TProjectImages> imagesList = projectInfoService.findImageList(project.getId());
        if (CollectionUtils.isEmpty(imagesList)) {
            imagesList = new ArrayList<>();
        }
        List<String> projectImages = new ArrayList<>();
        for (TProjectImages images : imagesList) {
            //判断图片类型，如果是头图 直接设置
            if (images.getImgtype() == 0) {
                projectDetailVo.setHeaderImage(images.getImgurl());
            } else {
                projectImages.add(images.getImgurl());
            }
        }
        //重新放置图片到VO中
        projectDetailVo.setDetailsImage(projectImages);

        //4.获得回报增量
        List<TReturn> returnList = projectInfoService.getReturnList(project.getId());
        projectDetailVo.setProjectReturns(returnList);

        return AppResponse.ok(projectDetailVo);

    }

    @ApiOperation("查询全部标签列表")
    @GetMapping("/findAllTag")
    public AppResponse<Object> findAllTag(){
        List<TTag> tagList = projectInfoService.findAllProjectTag();
        return AppResponse.ok(tagList);
    }

    @ApiOperation("查询全部分类列表")
    @GetMapping("/findAllType")
    public AppResponse<Object> findAllType(){
        List<TType> typeList = projectInfoService.findAllProjectType();
        return AppResponse.ok(typeList);
    }

    @ApiOperation("根据主键查询回报增量信息")
    @GetMapping("/findReturnInfo/{returnId}")
    public AppResponse<Object> findReturnInfo(@PathVariable(name = "returnId") Integer returnId){
        TReturn tReturn = projectInfoService.findReturn(returnId);
        return AppResponse.ok(tReturn);
    }




}
