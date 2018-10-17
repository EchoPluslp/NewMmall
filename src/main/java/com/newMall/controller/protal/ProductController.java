package com.newMall.controller.protal;

import com.github.pagehelper.PageInfo;
import com.newMall.common.ServerResponse;
import com.newMall.service.IProductService;
import com.newMall.vo.ProductDetailVo;
import com.newMall.vo.ProductListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Liupeng
 * @createTime 2018-10-17 22:42
 **/
@Controller
@RequestMapping("/product/")
public class ProductController {


    @Autowired
    private IProductService iProductService;

    @RequestMapping("/list.do")
    @ResponseBody
    public ServerResponse<PageInfo> getProductList(
                                                    @RequestParam(value = "categgoryId",required = false) Integer categoryId,
                                                   @RequestParam(value = "keyword",required = false) String keyword,
                                                   @RequestParam(value =  "pageNum",defaultValue = "0") Integer pageNum,
                                                   @RequestParam(value =  "pageSize",defaultValue = "10") Integer pageSize,
                                                   @RequestParam(value =  "orderby",defaultValue = "") String orderby) {
        return iProductService.getProductByKeyWordAndCategoryId(keyword, categoryId, pageNum, pageSize, orderby);
    }

    @RequestMapping("/detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> detail(Integer productId) {
        return iProductService.getProductDetail(productId);
    }
}