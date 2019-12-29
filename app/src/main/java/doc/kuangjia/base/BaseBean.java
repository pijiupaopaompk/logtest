package com.zkys.pad.launcher.base;

/**
 * 此类不能混淆
 */

public class BaseBean<T> {

    private int code;
    private String msg;
    private T data;
    private String sign;

    public boolean isSuccess() {
        if (code == 200) {
            return true;
        }
        return false;
    }

    public T getData() {
        if (code == 200 && data != null) {
            return data;
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }
}
