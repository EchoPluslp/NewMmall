package com.newMall.dao;

import com.newMall.common.ServerResponse;
import com.newMall.pojo.Category;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);


    List<Category> selectCategoryChildrenByParentId(Integer categoryId);

}