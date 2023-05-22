package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomerException;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.pojo.Dish;
import com.itheima.reggie.pojo.DishFlavor;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorService dishFlavorService;

    //自定义方法,分页查询菜品管理
    @Override
    public Page<DishDto> pageDishDto(Integer page, Integer pageSize, String name) {
        //创建分页对象
        Page<DishDto> pageInfo = new Page(page,pageSize);
        //调用Mapper分页方法查询
        pageInfo = dishMapper.pageDishDto(pageInfo,name);
        //返回pageInfo对象
        return pageInfo;
    }

    //修改菜品,显示菜品数据
    @Override
    public DishDto getByDishId(long id) {
        return dishMapper.getByDishId(id);
    }


    //自定义方法,保存菜品,菜品口味数据
    @Override
    @Transactional
    public void saveDishWithFlavor(DishDto dishDto) {
        //添加菜品数据,往dish表添加多条数据
        this.save(dishDto);
        //添加口味数据,往dish_flavor表中添加多条数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        //TODO 给每个口味设置菜品id
        flavors.forEach(flavor ->flavor.setDishId(dishDto.getId()));
        //批量保存
        dishFlavorService.saveBatch(flavors);
    }


    //菜品管理,删除,批量删除
    @Override
    @Transactional
    public void deleteByids(Long[] ids) {
        //根据ids,查询其中是否有菜品为开启销售
        Integer count = this.lambdaQuery()
                .in(Dish::getId)
                .eq(Dish::getStatus, 1)
                .count();
        //判断菜品是否为启售,则无法删除
        if (count > 0){
            throw new CustomerException("有菜品正在售卖,无法删除");
        }
        //判断为停售状态,封装到集合中
        this.removeByIds(Arrays.asList(ids));
        //删除菜品口味信息
        dishFlavorService.lambdaUpdate()
                .in(DishFlavor::getDishId,ids)
                .remove();
    }


    //保存菜品修改信息
    @Override
    @Transactional
    public void saveByIdWithFlavor(DishDto dishDto){
        //修改菜品数据,操作dish表,
        this.updateById(dishDto);
        //修改口味数据,操作dish表
        this.dishFlavorService.lambdaUpdate()
                .eq(DishFlavor::getDishId,dishDto.getId())
                .remove();
        List<DishFlavor> flavors = dishDto.getFlavors();
        //给每个口味设置dishid
        flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishDto.getId()));
        dishFlavorService.saveBatch(flavors);
    }


    //套餐管理,菜品列表
    @Override
    public List<DishDto> dishDtoList(Long categoryId, String name) {
        List<DishDto> dishDtoList = dishMapper.ListDishDto(categoryId,name);
        return dishDtoList;
    }

    //根据id查询套餐相对应的菜品信息
    @Override
    public List<DishDto> getDishBySetmealId(Long setmealId) {
        return dishMapper.getDishBySetmealId(setmealId);
    }
}


