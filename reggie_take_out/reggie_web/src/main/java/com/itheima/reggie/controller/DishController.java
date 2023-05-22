package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.pojo.Dish;
import com.itheima.reggie.service.DishService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CacheManager cacheManager;

    /**
     * //分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("page")
    public R<Page<DishDto>> page(Integer page,Integer pageSize,String name){
        //传统分页查询无法满足数据展示,因为要分类名称
        Page<DishDto> pageInfo = dishService.pageDishDto(page,pageSize,name);
        return R.success(pageInfo);
    }



    /**
     * //添加菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    @CacheEvict(cacheNames = "dish",allEntries = true)
    public R save(@RequestBody DishDto dishDto){
        dishService.saveDishWithFlavor(dishDto);
        return R.success("添加成功");
    }



    /**
     * //修改菜品 数据显示
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public R<DishDto> updateDish(@PathVariable long id){
        DishDto dishDto = dishService.getByDishId(id);
        return R.success(dishDto);
    }



    /**
     * 删除菜品/批量删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> Delete(Long[] ids){
        //批量删除菜品
        dishService.deleteByids(ids);
        return R.success("删除成功");
    }



    /**
     * 修改菜品/批量修改菜品状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("status/{status}")
    public R<String> updateStatus(@PathVariable Integer status,Long[] ids){
        //批量修改状态
        dishService.lambdaUpdate()
                .set(Dish::getStatus,status)
                .in(Dish::getId,ids)
                .update();
        return R.success("修改成功");
    }



    /**
     * 保存修改菜品信息
     * @param dishDto
     * @return
     */
    @PutMapping
    public R saveById(@RequestBody DishDto dishDto){

        dishService.saveByIdWithFlavor(dishDto);

        return R.success("保存成功");
    }



    /**
     * //套餐管理,菜品列表
     * @param categoryId
     * @return
     */
    @Cacheable(cacheNames = "dish",key = "#categoryId + '_' + #name")
    @GetMapping("list")
    public R<List<DishDto>> dishList(Long categoryId,String name){

    List<DishDto> list  = dishService.dishDtoList(categoryId,name);

    return R.success(list);
    }
}
