package com.newMall.service.impl;

import com.newMall.common.Const;
import com.newMall.common.ServerResponse;
import com.newMall.dao.UserMapper;
import com.newMall.pojo.User;
import com.newMall.service.IUserService;
import com.newMall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
       ServerResponse<String> username =  this.checkVaild(user.getUsername(), Const.USERNAME);//校验用户名是否存在
        if (!username.isSuccess()) {
            return username;
        }

        ServerResponse<String> email =  this.checkVaild(user.getEmail(), Const.EMAIL);//校验用户名是否存在
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
            }else if (Const.EMAIL.equals(type)) {
                checkCount = userMapper.checkEmail(str);
                if (checkCount > 0) {
                    return ServerResponse.createByErrorMsg("邮箱已存在");
                }
            }else{
                return ServerResponse.createByErrorMsg("参数错误");
            }
        }else {
            return ServerResponse.createByErrorMsg("参数错误");
        }
        return ServerResponse.createBySuccessMsg("校验成功");
    }
}