package com.newMall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.newMall.common.Const;
import com.newMall.common.ResponseCode;
import com.newMall.common.ServerResponse;
import com.newMall.dao.CategoryMapper;
import com.newMall.dao.ProductMapper;
import com.newMall.pojo.Category;
import com.newMall.pojo.Product;
import com.newMall.service.ICategoryService;
import com.newMall.service.IProductService;
import com.newMall.util.DateTimeUtil;
import com.newMall.util.PropertiesUtil;
import com.newMall.vo.ProductDetailVo;
import com.newMall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Liupeng
 * @createTime 2018-10-16 21:45
 **/
@Service("iProductService")
public class ProductServiceImpl  implements IProductService {


    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService categoryService;
    @Override
    public ServerResponse getProductList(int pageNum, int pageSize) {
        //启动pageHelper
        PageHelper.startPage(pageNum, pageSize);

        //生成要展示的数据
        List<ProductListVo> productListVos = Lists.newArrayList();
        //获取数据
        List<Product> productItem = productMapper.selectList();
        for (Product product:
                productItem) {
            ProductListVo productListVo = assembleProductListVo(product);
            productListVos.add(productListVo);
        }
        //设置pageHelper的信息
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(productListVos);
        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        //模糊查询
        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        //id查询，注意<where> 标签处理
        List<Product> products = productMapper.selectByProductNameAndProductId(productName, productId);
        List<ProductListVo> productListVos = Lists.newArrayList();

        for (Product product :
                products) {
            ProductListVo productListVo = assembleProductListVo(product);
            productListVos.add(productListVo);
        }

        //pageInfo收尾工作
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(productListVos);
        return ServerResponse.createBySuccess(pageInfo);
    }


    //前台获取商品详情信息
    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorMsg(ResponseCode.ILLEGAL_ARGUMENT.getMsg());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMsg("商品已经下架或者删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    //getProductByKeyWordAndCategoryId
    public ServerResponse<PageInfo> getProductByKeyWordAndCategoryId(String keyword, Integer categoryId, Integer pageNum, Integer pageSize, String orderby) {
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getMsg());
        }
        List<Integer> categoryList = null;

        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)) {
                //返回空的集合，不会返回错误!
                List<ProductListVo> productListVos = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVos);
                return ServerResponse.createBySuccess(pageInfo);
            }
            categoryList = categoryService.getDeepCategoryById(categoryId).getDate();
        }
        //判断非空处理
        if (StringUtils.isBlank(keyword)) {
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }

        PageHelper.startPage(pageNum, pageSize);
        //排序处理
        if (StringUtils.isNotBlank(orderby)) {
            if (Const.ProductListOrderBy.PRIACE_ASC_DESC.contains(orderby)) {
                String[] split = orderby.split("_");
                PageHelper.orderBy(split[0] + " " + split[1]);
            }
        }

        List<Product> products = productMapper.selectByProductNameAndCategory(StringUtils.isBlank(keyword)?null:keyword,
                categoryList.size()==0?null:categoryList);
        List<ProductListVo> productListVos = Lists.newArrayList();
        //构建listVo
        for (Product productItem :
                products) {
            productListVos.add(assembleProductListVo(productItem));
        }
        PageInfo pageInfo = new PageInfo(productListVos);

        //mybatis操作
        return ServerResponse.createBySuccess(pageInfo);
    }
    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }

    /**
     * 初始化vo 值对象
     * 组装商品详情Vo
     * @param product
     * @return
     */
    private ProductDetailVo assembleProductDetailVo(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();

        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImage(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        //imageHost
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        //得到parentCategoryId
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null){
            productDetailVo.setParentCategoryId(0);//默认为根节点
        }else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        //createTime
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        //updateTime
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

}