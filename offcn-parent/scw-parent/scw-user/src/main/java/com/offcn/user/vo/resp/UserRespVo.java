package com.offcn.user.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther: lhq
 * @Date: 2020/9/6 16:26
 * @Description: 用户登录成功返回响应的信息封装类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class UserRespVo implements Serializable {

    @ApiModelProperty(value = "访问令牌，请妥善保管，以后每次请求都要带上")
    private String accessToken;
    private String loginacct;

    private String username;

    private String email;

    private String authstatus;

    private String usertype;

    private String realname;

    private String cardnum;

    private String accttype;

}
