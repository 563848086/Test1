package com.offcn.user.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther: lhq
 * @Date: 2020/9/6 11:47
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("测试实体类")
public class User implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "年龄")
    private Integer age;
}
