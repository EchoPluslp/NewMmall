package com.newMall.controller;

import com.newMall.common.Const;
import com.newMall.common.ResponseCode;
import com.newMall.common.ServerResponse;
import com.newMall.pojo.User;
import com.newMall.service.IUserService;
import com.newMall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
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

    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
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
    @RequestMapping(value = "/forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    //重置密码
    @RequestMapping("/forget_reset_password.do")
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
    }

    //登录状态下的重置密码
    @RequestMapping("/reset_password.do")
    @ResponseBody
    public ServerResponse<String> ResetPassword(String password, String passwordNew, HttpSession session) {
        User user = (User) session.getAttribute(Const.CUEEENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getMsg());
        }
        return iUserService.ResetPassword(password, passwordNew, user);
    }

    //登录状态下更新个人信息
    @RequestMapping("/update_information.do")
    @ResponseBody
    public ServerResponse<String> updateInformation(String email, String phone,
                                                    String question, String answer, HttpSession session) {
        User user = (User) session.getAttribute(Const.CUEEENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getMsg());
        }
        if (!checkValid(email, Const.EMAIL).isSuccess()) {
            return ServerResponse.createByErrorMsg("Email已经存在，请重新绑定新的email");

        }
        User user1 = new User();
        user1.setPassword(email);
        user1.setPhone(phone);
        user1.setPassword(MD5Util.MD5EncodeUtf8(question));
        user1.setAnswer(answer);
        return iUserService.updateInformation(user1);
    }

    @RequestMapping("get_information.do")
    @ResponseBody
    public ServerResponse<User> GetInformation(HttpSession session) {
        User user = (User) session.getAttribute(Const.CUEEENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("用户不存在，请登录");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }




}