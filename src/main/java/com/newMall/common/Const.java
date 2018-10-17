package com.newMall.common;

import com.google.common.collect.Sets;

import java.util.Set;

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

    public interface ProductListOrderBy {
        Set<String> PRIACE_ASC_DESC = Sets.newHashSet("price_asc", "price_desc");
    }

    public enum productStatus{
        ON_SALE(1, "销售中"),
        ;
        private int code;
        private String msg;

        productStatus(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }
}