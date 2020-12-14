package com.offcn.dycommon.response;

import com.offcn.dycommon.enums.ResponseCodeEnums;

/**
 * @Auther: lhq
 * @Date: 2020/9/6 14:33
 * @Description: 应用统一返回结果数据封装类
 */
public class AppResponse<T> {

    private Integer code;
    private String msg;
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    //操作成功
    public static<T> AppResponse<T> ok(T data){
        AppResponse resp = new AppResponse();
        resp.setCode(ResponseCodeEnums.SUCCESS.getCode());
        resp.setMsg(ResponseCodeEnums.SUCCESS.getMsg());
        resp.setData(data);
        return  resp;
    }
    //操作失败
    public static<T> AppResponse<T> fail(T data){
        AppResponse resp = new AppResponse();
        resp.setCode(ResponseCodeEnums.FAIL.getCode());
        resp.setMsg(ResponseCodeEnums.FAIL.getMsg());
        resp.setData(data);
        return  resp;
    }
}
