package com.offcn.user.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther: lhq
 * @Date: 2020/9/6 15:29
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户VO实体类")
public class UserRegisterVo implements Serializable {

    @ApiModelProperty("手机号")
    private String loginacct;
    @ApiModelProperty("密码")
    private String userpswd;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("验证码")
    private String code;

}
