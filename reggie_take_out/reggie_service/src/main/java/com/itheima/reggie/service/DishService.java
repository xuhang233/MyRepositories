package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.pojo.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    //分页查询方法,以及菜品分类名称
    Page<DishDto> pageDishDto(Integer page, Integer pageSize, String name);

    //修改菜品,显示菜品数据
    DishDto getByDishId(long id);

    //自定义方法,保存菜品,菜品口味数据
    void saveDishWithFlavor(DishDto dishDto);

    //菜品管理,删除,批量删除
    void deleteByids(Long[] ids);

    //保存菜品修改信息
    void saveByIdWithFlavor(DishDto dishDto);

    //套餐管理,菜品列表
    List<DishDto> dishDtoList(Long categoryId, String name);

    //根据id查询套餐相对应的菜品信息
    List<DishDto> getDishBySetmealId(Long setmealId);
}
