package com.bxlt.converter.util;

import lombok.Data;

import java.util.List;

/**
 * Created by Administrator on 2017/6/19.
 */
@Data
public class ResultEntity {

    /**
     * 是否操作成功
     */
    private Boolean success;
    /**
     * token
     */
    private String token;
    /**
     * 数据
     */
    private List data;
    /**
     * 错误代码
     */
    private int errorCode;

    /**
     * 提示信息
     */
    private String msg;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
