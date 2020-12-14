package com.offcn.order.service.impl;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.service.ProjectInfoServiceFeign;
import com.offcn.order.vo.resp.TReturn;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/9/8 10:32
 * @Description:
 */
@Component
    public class ProjectInfoServiceFeignException implements ProjectInfoServiceFeign {
    @Override
    public AppResponse<List<TReturn>> getReturnList(Integer projectId) {
        AppResponse<List<TReturn>> response = AppResponse.fail(null);
        response.setMsg("服务异常");
        return response;
    }
}
