package com.offcn.user.exception;

import com.offcn.user.enums.UserExceptionEnum;

/**
 * @Auther: lhq
 * @Date: 2020/9/6 15:40
 * @Description: 自定义异常类
 */
public class UserException extends RuntimeException{

    public UserException(UserExceptionEnum userExceptionEnum){
        super(userExceptionEnum.getMsg());
    }
}
