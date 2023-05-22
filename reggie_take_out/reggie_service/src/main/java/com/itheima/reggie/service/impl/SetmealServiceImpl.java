package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomerException;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.pojo.Setmeal;
import com.itheima.reggie.pojo.SetmealDish;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealMapper setmealMapper;


    //保存添加套餐数据
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        //获取套餐集合
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //添加套餐关联的菜品数据
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealDto.getId()));
        //保存数据
        setmealDishService.saveBatch(setmealDishes);
    }

    //套餐管理分页查询
    @Override
    public Page<SetmealDto> pageWithSetmealDto(Integer page, Integer pageSize, String name) {
        //创建分页对象
        Page<SetmealDto> pageInfo = new Page(page,pageSize);
        //调用Mapper分页方法查询
        pageInfo = setmealMapper.pageWithSetmealDto(pageInfo,name);
        //返回pageInfo对象
        return pageInfo;
    }


    //套餐管理,批量,单一,删除
    @Override
    public void deleteIds(Long[] ids) {
        //根据ids,查询其中是否有菜品为开启销售
        Integer count = this.lambdaQuery()
                .in(Setmeal::getId,ids)
                .eq(Setmeal::getStatus, 1)
                .count();
        //判断菜品是否为启售,则无法删除
        if (count >0 ){
            throw new CustomerException("有菜品正在售卖,无法删除");
        }
        //判断为停售状态,封装到集合中
        this.removeByIds(Arrays.asList(ids));
        //删除菜品口味信息
        setmealDishService.lambdaUpdate()
                .in(SetmealDish::getId,ids)
                .remove();
    }

    //修改套餐,显示数据
    @Override
    public SetmealDto selectWithDtoId(Long id) {
        return setmealMapper.selectWithDtoId(id);
    }


    //套餐管理修改保存
    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        //更新套餐数据,操作Setmeal表
        this.updateById(setmealDto);
        //更新套餐菜品关联数据
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishService.lambdaUpdate()
                .eq(SetmealDish::getSetmealId,setmealDto.getId())
                .remove();
        //TODO给每个SetmealDish设置套餐Id
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealDto.getId()));
        //批量保存
        setmealDishService.saveBatch(setmealDishes);
    }


}
