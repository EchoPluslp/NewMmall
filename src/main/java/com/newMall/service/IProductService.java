package com.newMall.service;

import com.github.pagehelper.PageInfo;
import com.newMall.common.ServerResponse;
import com.newMall.vo.ProductDetailVo;
import com.newMall.vo.ProductListVo;

/**
 * @author Liupeng
 * @create 2018-10-16 21:45
 **/
public interface IProductService {

    ServerResponse getProductList(int pageNum, int pageSize);

    ServerResponse searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductByKeyWordAndCategoryId(String keyword, Integer categoryId, Integer pageNum, Integer pageSize,String orderby);
}
