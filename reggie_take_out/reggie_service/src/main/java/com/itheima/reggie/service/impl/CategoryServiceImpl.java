package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomerException;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.pojo.Category;
import com.itheima.reggie.pojo.Dish;
import com.itheima.reggie.pojo.Setmeal;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void deleteByid(Long id) {
        //
        Integer count = dishService.lambdaQuery().eq(Dish::getCategoryId, id).count();
        if (count>0){
            throw new CustomerException("该分类不能删除,关联了其他菜品");
        }
        //
        Integer count1 = setmealService.lambdaQuery().eq(Setmeal::getCategoryId, id).count();
        if (count1>0){
            throw new CustomerException("该分类不能删除,关联了其他套餐");
        }
        //
        this.removeById(id);
    }
}
