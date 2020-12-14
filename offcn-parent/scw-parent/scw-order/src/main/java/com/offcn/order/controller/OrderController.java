package com.offcn.order.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.pojo.TOrder;
import com.offcn.order.service.OrderService;
import com.offcn.order.vo.req.OrderInfoSubmitVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: lhq
 * @Date: 2020/9/8 10:39
 * @Description:
 */
@RestController
@RequestMapping("/order")
@Api(tags = "订单模块")
@Slf4j
public class OrderController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private OrderService orderService;


    @ApiOperation("保存订单")
    @PostMapping("/saveOrder")
    public AppResponse<Object> saveOrder(@RequestBody OrderInfoSubmitVo vo) {
        String memberId = redisTemplate.opsForValue().get(vo.getAccessToken());
        if (memberId == null) {
            return AppResponse.fail("无此权限，请先登录");
        }
        try {
            TOrder order = orderService.saveOrder(vo);
            return AppResponse.ok(order);
        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.fail(null);
        }

    }

}
