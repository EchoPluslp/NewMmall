package com.newMall.service;

import com.newMall.common.ServerResponse;
import com.newMall.pojo.User;

/**
 * @author Liupeng
 * @create 2018-10-09 22:32
 **/
public interface IUserService {
    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkVaild(String str, String type);
}
