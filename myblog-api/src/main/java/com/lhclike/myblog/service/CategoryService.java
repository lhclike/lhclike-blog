package com.lhclike.myblog.service;

import com.lhclike.myblog.vo.CategoryVo;
import com.lhclike.myblog.vo.Result;


public interface CategoryService {

    CategoryVo findCategoryById(Long id);
    Result findAll();
    Result findAllDetail();
    Result categoriesDetailById(Long id);
}
