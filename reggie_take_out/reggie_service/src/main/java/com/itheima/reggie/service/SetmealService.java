package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.pojo.Setmeal;

public interface SetmealService extends IService<Setmeal> {

    //套餐添加保存
    void saveWithDish(SetmealDto setmealDto);

    //套餐管理分页查询
    Page<SetmealDto> pageWithSetmealDto(Integer page, Integer pageSize, String name);


    //套餐管理,批量,单一,删除
    void deleteIds(Long[] ids);


    //修改套餐,显示数据
    SetmealDto selectWithDtoId(Long id);


    //套餐管理修改保存
    void updateWithDish(SetmealDto setmealDto);
}
