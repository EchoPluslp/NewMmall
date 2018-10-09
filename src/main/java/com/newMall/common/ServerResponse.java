package com.newMall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;


/**
 * 高复用服务器相应对象
 *
 * @author Liupeng
 * @createTime 2018-10-09 22:02
 **/
//设置json序列化时，如果是null的对象，key也会消失，即不会显示为value为null的键值对
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> {
    private int status;
    private String msg;
    private T date;

    //如果成功返回的消息
    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMsg(String msg) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg, T data) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    //如果失败返回的信息
    public static <T> ServerResponse<T> createByError() {
        return new ServerResponse<>(ResponseCode.ERROR.getCode());
    }

    public static <T> ServerResponse<T> createByErrorMsg(String errorMsg) {
        return new ServerResponse<>(ResponseCode.ERROR.getCode(), errorMsg);
    }

    public static <T> ServerResponse<T> createByErrorCodeMsg(int code, String msg) {
        return new ServerResponse<>(code, msg);
    }



    //判断消息是否成功
    @JsonIgnore //序列化后不会显示该字段
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    //构造函数设置------------start
    private ServerResponse(int status, String msg, T date) {
        this.status = status;
        this.msg = msg;
        this.date = date;
    }

    private ServerResponse(int status) {
        this.status = status;
    }

    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(int status, T date) {
        this.status = status;
        this.date = date;
    }
    //------------end


    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getDate() {
        return date;
    }
}