package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.pojo.Setmeal;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {

    //套餐管理,分页查询
    Page<SetmealDto> pageWithSetmealDto(Page<SetmealDto> pageInfo, String name);

    //修改套餐,显示数据
    SetmealDto selectWithDtoId(Long id);
}


