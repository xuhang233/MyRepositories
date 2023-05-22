package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.pojo.Category;

public interface CategoryService extends IService<Category> {

    //自定义删除方法
    public void deleteByid(Long id);

}
