package com.newMall.common;

/**
 * @author Liupeng
 * @createTime 2018-10-09 23:13
 **/
public class Const {
    public static final String CUEEENT_USER = "currentUser";


    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface Role{
        int ROLE_CUSTOMER = 0;//普通用户
        int ROLE_ADMIN = 1;//管理员
    }
}