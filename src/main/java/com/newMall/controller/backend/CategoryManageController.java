package com.newMall.controller.backend;

import com.newMall.common.Const;
import com.newMall.common.ResponseCode;
import com.newMall.common.ServerResponse;
import com.newMall.pojo.User;
import com.newMall.service.ICategoryService;
import com.newMall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author Liupeng
 * @createTime 2018-10-14 23:33
 **/
@RequestMapping("/manage/category")
@Controller
public class CategoryManageController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping("/get_category.do")
    @ResponseBody
    public ServerResponse get_category(@RequestParam(value = "C=categoryId", defaultValue = "0") Integer CategoryId,
                                       HttpSession session) {
        User user = (User) session.getAttribute(Const.CUEEENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg(ResponseCode.NEED_LOGIN.getMsg());
        }
        if (user.getRole().equals(Const.Role.ROLE_ADMIN)) {
            return iCategoryService.getCategoryByParentId(CategoryId);
        }else{
            return ServerResponse.createByErrorMsg("非管理员，无权限操作");
        }
    }

    @RequestMapping("/add_category.do")
    @ResponseBody
    public ServerResponse add_category(HttpSession session, @RequestParam(value = "parentId", defaultValue = "0")
            Integer parentId, String CategoryName) {
        User user = (User) session.getAttribute(Const.CUEEENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg(ResponseCode.NEED_LOGIN.getMsg());
        }
        if (user.getRole().equals(Const.Role.ROLE_ADMIN)) {
            return iCategoryService.insertCategoryByParentIdAndName(parentId,CategoryName);
        }else{
            return ServerResponse.createByErrorMsg("非管理员，无权限操作");
        }
    }


    @RequestMapping("/set_category_name.do")
    @ResponseBody
    public ServerResponse<String> set_category_name(HttpSession session,
                                            @RequestParam(value = "parentId", defaultValue = "0")
                                            Integer categoryId,String categoryName) {
        User user = (User) session.getAttribute(Const.CUEEENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg(ResponseCode.NEED_LOGIN.getMsg());
        }
        if (user.getRole().equals(Const.Role.ROLE_ADMIN)) {
            return iCategoryService.updateByCategoryIdAndName(categoryId, categoryName);
        }else{
            return ServerResponse.createByErrorMsg("非管理员，无权限操作");
        }
    }

    @RequestMapping("/get_deep_category.do")
    @ResponseBody
    public ServerResponse get_deep_category(HttpSession session,Integer categoryId) {
        User user = (User) session.getAttribute(Const.CUEEENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg(ResponseCode.NEED_LOGIN.getMsg());
        }
        if (user.getRole().equals(Const.Role.ROLE_ADMIN)) {
            return iCategoryService.getDeepCategoryById(categoryId);
        }else{
            return ServerResponse.createByErrorMsg("非管理员，无权限操作");
        }
    }


}