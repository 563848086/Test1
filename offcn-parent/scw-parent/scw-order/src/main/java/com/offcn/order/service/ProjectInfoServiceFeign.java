package com.offcn.order.service;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.service.impl.ProjectInfoServiceFeignException;
import com.offcn.order.vo.resp.TReturn;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/9/8 10:28
 * @Description:
 */
@FeignClient(value = "SCWPROJECT",fallback = ProjectInfoServiceFeignException.class)
public interface ProjectInfoServiceFeign{

    //要与服务提供者Controller中的方法一致
    @GetMapping("/project/details/getReturnList/{projectId}")
    public AppResponse<List<TReturn>> getReturnList(@PathVariable(name = "projectId") Integer projectId);
}
