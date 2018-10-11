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

    //检查email或者username是否存在
    @RequestMapping("/checkvalid.do")
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkVaild(str, type);
    }

    //获取用户详情信息
    @RequestMapping("/get_user_info.do")
    @ResponseBody
    public ServerResponse<User> get_user_info(HttpSession session) {
        User CurrentUser = (User) session.getAttribute(Const.CUEEENT_USER);
        if (CurrentUser == null) {
            return ServerResponse.createByErrorMsg("用户未登录,无法获取当前用户信息");
        }
        return ServerResponse.createBySuccess(CurrentUser);
    }

    //忘记密码提示问题
    @RequestMapping("/forget_get_question.do")
    @ResponseBody
    public ServerResponse<String> forget_get_question(String username) {
        return iUserService.forgettGetQuestion(username);
    }

    //检验密码提示问题答案是否正确,如果正确返回一个token
    @RequestMapping(value = "/forget_check_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    @RequestMapping("/forget_reset_password.do")
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
    }



}