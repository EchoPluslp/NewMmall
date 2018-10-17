package com.newMall.controller.backend;

import com.newMall.common.Const;
import com.newMall.common.ResponseCode;
import com.newMall.common.ServerResponse;
import com.newMall.dao.ProductMapper;
import com.newMall.pojo.User;
import com.newMall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author Liupeng
 * @createTime 2018-10-16 21:44
 **/
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IProductService iProductService;

    @RequestMapping("/list.do")
    @ResponseBody
    public ServerResponse list(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                HttpSession session) {
        User user = (User) session.getAttribute(Const.CUEEENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg(ResponseCode.NEED_LOGIN.getMsg());
        }
        if (user.getRole().equals(Const.Role.ROLE_ADMIN)) {
            return iProductService.getProductList(pageNum,pageSize);
        }else{
            return ServerResponse.createByErrorMsg("管理员操作，你无权限");
        }
    }

    @RequestMapping("/search.do")
    @ResponseBody
    public ServerResponse search(HttpSession session, String productName, Integer productId,
                                 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(Const.CUEEENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg(ResponseCode.NEED_LOGIN.getMsg());
        }
        if (user.getRole().equals(Const.Role.ROLE_ADMIN)) {
            return iProductService.searchProduct(productName, productId, pageNum, pageSize);
        }else{
            return ServerResponse.createByErrorMsg("管理员操作，你无权限");
        }
    }
}