package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.pojo.Setmeal;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private DishService dishService;

    @Autowired
    private CacheManager cacheManager;


    /**
     * //根据条件,显示套餐列表
     * @return
     */
    @GetMapping("list")
    @Cacheable(cacheNames = "setmealCache",key = "#categoryId",unless = "#result.data==0")
    public R<List<Setmeal>>lishDishDto(Long categoryId,Integer status){
        //
        List<Setmeal> list = setmealService.lambdaQuery()
                .eq(categoryId != null, Setmeal::getCategoryId, categoryId)
                .eq(Setmeal::getStatus, 1)
                .list();
        //
        return R.success(list);
    }


    /**
     *  //套餐添加保存
     * @param setmealDto
     * @return
     */
    @PostMapping
    @CacheEvict(value = "setmealCache",allEntries = true) //清除setmealCache名称下,所有的缓存数据
    public R<String> setmealSave(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return R.success("添加成功");
    }



    /**
     *  //套餐管理分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("page")
    public R<Page<SetmealDto>> page(Integer page, Integer pageSize, String name){
        Page<SetmealDto> pageInfo = setmealService.pageWithSetmealDto(page,pageSize,name);
        return R.success(pageInfo);
    }


    /**
     * //套餐管理,批量,起售 停售
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("status/{status}")
    public R<String> updateStatus(@PathVariable Integer status,Long[] ids){
        setmealService.lambdaUpdate()
                .set(Setmeal::getStatus,status)
                .in(Setmeal::getId,ids)
                .update();
        return R.success(" 修改成功");
    }


    /**
     *  //套餐管理,批量,单一,删除
     * @param ids
     * @return
     */
    @DeleteMapping
    @CacheEvict(value = "setmealCache",allEntries = true) //清除setmealCache名称下,所有的缓存数据
    public R<String> deldetIds(Long[] ids){
        setmealService.deleteIds(ids);
        return R.success("删除成功");
    }



    /**
     * //修改套餐,显示数据
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public R<SetmealDto> selectBySetmealDto(@PathVariable Long id){
        SetmealDto setmealDto =  setmealService.selectWithDtoId(id);
        return R.success(setmealDto);
    }


    /**
     * //套餐管理修改保存
     * @param setmealDto
     * @return
     */
    @PutMapping
    @CacheEvict(value = "setmealCache",allEntries = true) //清除setmealCache名称下,所有的缓存数据
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithDish(setmealDto);
        return R.success("修改成功");
    }




    /**
     *  //根据id查询套餐相对应的菜品信息
     * @param setmealId
     * @return
     */
    @GetMapping("dish/{setmealId}")
    public R<List<DishDto>> getDishBySetmealId(@PathVariable Long setmealId){
        List<DishDto> dishDtoList = dishService.getDishBySetmealId(setmealId);
        return R.success(dishDtoList);
    }
}
