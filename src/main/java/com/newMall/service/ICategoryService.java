package com.newMall.service;

import com.newMall.common.ServerResponse;

/**
 * @author Liupeng
 * @create 2018-10-14 23:34
 **/
public interface ICategoryService {
    ServerResponse getCategoryByParentId(Integer categoryId);

    ServerResponse insertCategoryByParentIdAndName(Integer parentId,String CategoryName);

    ServerResponse<String> updateByCategoryIdAndName(Integer categoryId, String categoryName);

    ServerResponse getDeepCategoryById(Integer categoryId);
}
