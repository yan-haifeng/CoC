package com.coc.middleware.utils;

import lombok.Data;

//统一返回结果的类
@Data
public class R {
    private Boolean success;

    private Integer code;

    private String message;

    private Object data;

    //把构造方法私有   不让别人new该类
    private R() {}
    //实现链式编程
    //R.ok().code().message().data();

    //成功静态方法
    public static R ok() {
        R r = new R();
        r.setSuccess(true);
        r.setCode(200);
        r.setMessage("成功");
        return r;
    }

    //失败静态方法
    public static R error() {
        R r = new R();
        r.setSuccess(false);
        r.setCode(400);
        r.setMessage("失败");
        return r;
    }

    public R success(Boolean success){
        this.setSuccess(success);
        return this;   //返回this  实现链式编程
    }

    //实现链式编程
    //R.ok().code().message().data();

    public R message(String message){
        this.setMessage(message);
        return this;
    }

    public R code(Integer code){
        this.setCode(code);
        return this;
    }

    public R data(Object data){
        this.data = data;
        return this;
    }
}