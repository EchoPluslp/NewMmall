package com.newMall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.newMall.common.ServerResponse;
import com.newMall.dao.CategoryMapper;
import com.newMall.pojo.Category;
import com.newMall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author Liupeng
 * @createTime 2018-10-14 23:34
 **/
public class CategoruServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    private Logger logger = LoggerFactory.getLogger(CategoruServiceImpl.class);

    @Override
    public ServerResponse<List<Category>> getCategoryByParentId(Integer categoryId) {
        List<Category> categories = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if (CollectionUtils.isEmpty(categories)) {
            logger.info("未能获取当前父节点的子节点");
        }
        return ServerResponse.createBySuccess(categories);
    }

    @Override
    public ServerResponse insertCategoryByParentIdAndName(Integer parentId, String categoryName) {
        if (StringUtils.isBlank(categoryName) || parentId == null) {
            return ServerResponse.createByErrorMsg("新建品类的参数错误");
        }
        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        category.setStatus(true);
        int count = categoryMapper.insertSelective(category);
        if (count > 0) {
            return ServerResponse.createBySuccessMsg("新建品类成功");
        }
        return ServerResponse.createByErrorMsg("新建品类失败");
    }

    @Override
    public ServerResponse<String> updateByCategoryIdAndName(Integer categoryId, String categoryName) {

        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int count = categoryMapper.updateByPrimaryKeySelective(category);
        if (count > 0) {
            return ServerResponse.createBySuccessMsg("更新品类成功");
        }
        return ServerResponse.createByErrorMsg("更新品类失败");
    }

    @Override
    public ServerResponse<List<Integer>> getDeepCategoryById(Integer categoryId) {
        Set<Category> categories = Sets.newHashSet();
        categories = findChildCategory(categories,categoryId);
        List<Integer> categoryList = Lists.newArrayList();
        if (categoryId != null) {
            for (Category c :
                    categories) {
                categoryList.add(c.getId());
            }
        }

        return ServerResponse.createBySuccess(categoryList);
    }

    private Set<Category> findChildCategory(Set<Category> categories,Integer categoryId) {
        //自己调用自己
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categories.add(category);
        }
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryItem :
                categoryList) {
            findChildCategory(categories, categoryItem.getId());
        }
        return categories;
    }
}