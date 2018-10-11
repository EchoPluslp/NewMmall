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

    ServerResponse<String> forgettGetQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String question, String answer);

    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    ServerResponse<String> ResetPassword(String password, String passwordNew, User user);

    ServerResponse<String> updateInformation(User user1);
}
