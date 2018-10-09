package com.newMall.controller;

import com.newMall.common.Const;
import com.newMall.common.ServerResponse;
import com.newMall.dao.UserMapper;
import com.newMall.pojo.User;
import com.newMall.service.IUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author Liupeng
 * @createTime 2018-10-09 22:27
 **/
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "/login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password,HttpSession session) {
        ServerResponse<User> user = iUserService.login(username, password);
        if (user.isSuccess()) {
            session.setAttribute(Const.CUEEENT_USER, user);
        }
        return user;
    }

    //退出登录
    @RequestMapping("/logout.do")
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CUEEENT_USER);
        return ServerResponse.createBySuccess();
    }

    //注册接口
    @RequestMapping("/register.do")
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    @RequestMapping("/checkvalid.do")
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkVaild(str, type);
    }
}