package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.pojo.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    //分页查询 菜品管理
    Page<DishDto> pageDishDto(@Param("page") Page<DishDto> pageInfo,
                              @Param("name") String name);

    //根据id查询菜品数据
    DishDto getByDishId(long id);

    //修复方法,根据分类查询菜品列表(包含口味数据)
    List<DishDto> ListDishDto(@Param("categoryId") Long categoryId,
                              @Param("name") String name);

    //根据id查询套餐相对应的菜品信息
    List<DishDto> getDishBySetmealId(Long setmealId);
}
