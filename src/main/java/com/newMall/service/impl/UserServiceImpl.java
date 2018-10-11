package com.newMall.service.impl;

import com.newMall.common.Const;
import com.newMall.common.ServerResponse;
import com.newMall.common.TokenCahce;
import com.newMall.dao.UserMapper;
import com.newMall.pojo.User;
import com.newMall.service.IUserService;
import com.newMall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.newMall.common.Const.Role.ROLE_CUSTOMER;

/**
 * @author Liupeng
 * @createTime 2018-10-09 22:32
 **/
@Service("iUserService")
public class UserServiceImpl implements IUserService {


    @Autowired
    private UserMapper userMapper;


    @Override
    public ServerResponse login(String username, String password) {
        int resultcount = userMapper.checkUsername(username);
        if (resultcount == 0) {
            return ServerResponse.createByErrorMsg("用户名不存在");
        }
        String md5password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.checkUsernamePassword(username, md5password);
        if (user == null) {
            return ServerResponse.createByErrorMsg("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);//将当前用户密码设置为空
        return ServerResponse.createBySuccess();
    }

    public ServerResponse register(User user) {
        //判断用户名是否存在start--------
        ServerResponse<String> username = this.checkVaild(user.getUsername(), Const.USERNAME);//校验用户名是否存在
        if (!username.isSuccess()) {
            return username;
        }

        ServerResponse<String> email = this.checkVaild(user.getEmail(), Const.EMAIL);//校验用户名是否存在
        if (!email.isSuccess()) {
            return email;
        }
        //--------------------end

        //设置权限
        user.setRole(ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int insertUser = userMapper.insertSelective(user);
        if (insertUser == 0) {
            return ServerResponse.createByErrorMsg("注册失败");
        }
        return ServerResponse.createBySuccessMsg("注册成功");
    }

    public ServerResponse<String> checkVaild(String str, String type) {
        if (StringUtils.isNotBlank(str)) {
            //开始校验
            int checkCount = 0;
            if (Const.USERNAME.equals(type)) {
                checkCount = userMapper.checkUsername(str);
                if (checkCount > 0) {
                    return ServerResponse.createByErrorMsg("用户名已存在");
                }
            } else if (Const.EMAIL.equals(type)) {
                checkCount = userMapper.checkEmail(str);
                if (checkCount > 0) {
                    return ServerResponse.createByErrorMsg("邮箱已存在");
                }
            } else {
                return ServerResponse.createByErrorMsg("参数错误");
            }
        } else {
            return ServerResponse.createByErrorMsg("参数错误");
        }
        return ServerResponse.createBySuccessMsg("校验成功");
    }

    @Override
    public ServerResponse<String> forgettGetQuestion(String username) {
        ServerResponse serverResponse = this.checkVaild(username, Const.USERNAME);
        if (serverResponse.isSuccess()) {
            return ServerResponse.createByErrorMsg("用户名不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isBlank(question)) {
            return ServerResponse.createByErrorMsg("该用户未设置找回密码问题");
        }
        return ServerResponse.createBySuccess(question);
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int result = userMapper.checkAnswer(username, question, answer);
        if (result > 0) {
            //问题答案正确，返回token值
            String forgetToken = UUID.randomUUID().toString();
            TokenCahce.setKey(TokenCahce.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMsg("密码提示问题答案错误");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        //判断用户名是否同时存在
        ServerResponse<String> user = this.checkVaild(username, Const.USERNAME);
        if (!user.isSuccess()) {
            return ServerResponse.createByErrorMsg("用户名不存在，请确认用户信息");
        }
        //判断token是否存在
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMsg("token不存在或者已过期，请重新上一步操作");
        }
        //判断本地缓存中的token是否存在
        String LocalToken = TokenCahce.getKey(TokenCahce.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(username)) {
            return ServerResponse.createByErrorMsg("token无效或者过期");
        }
        //此时，判断当前token和缓存中的token是否相同
        String Md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
        if (StringUtils.equals(forgetToken, LocalToken)) {
            //执行更新操作
            int count = userMapper.updatePasswordByUsername(username, Md5Password);
            if (count > 0) {
                return ServerResponse.createBySuccessMsg("修改密码成功");
            }
        } else {
            return ServerResponse.createByErrorMsg("Token不相同，请重新获取token");
        }
        return ServerResponse.createByErrorMsg("重置密码错误");
    }

    @Override
    public ServerResponse<String> ResetPassword(String password, String passwordNew, User user) {
        //检验旧密码是否正确
        User oldPass = userMapper.checkUsernamePassword(user.getUsername(), MD5Util.MD5EncodeUtf8(password));
        if (oldPass == null) {
            return ServerResponse.createByErrorMsg("旧密码错误");
        }
        String Md5Password = MD5Util.MD5EncodeUtf8(password);
        user.setPassword(Md5Password);
        int count = userMapper.updateByPrimaryKeySelective(user);
        if (count > 0) {
            return ServerResponse.createBySuccessMsg("修改密码成功");
        }
        return ServerResponse.createByErrorMsg( "修改密码失败");
    }

    @Override
    public ServerResponse<String> updateInformation(User user) {
        int count = userMapper.updateByPrimaryKeySelective(user);
        if (count > 0) {
            return ServerResponse.createBySuccessMsg("更新个人信息成功");
        }
        return ServerResponse.createByErrorMsg("更新个人信息失败");
    }

}