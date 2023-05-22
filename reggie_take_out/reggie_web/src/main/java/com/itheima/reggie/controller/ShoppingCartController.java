package com.itheima.reggie.controller;

import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.pojo.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 购物车管理
 */
@RestController
@RequestMapping("shoppingCart")
public class ShoppingCartController {


    @Autowired
    private ShoppingCartService shoppingCartService;



    /**
     * //查询用户购物车
     * @return
     */
    @GetMapping("list")
    public R<List<ShoppingCart>> list(){
        long userId = BaseContext.get();
        //
        List<ShoppingCart> list = shoppingCartService.lambdaQuery()
                .eq(ShoppingCart::getUserId, userId)
                .list();
        return R.success(list);
    }




    /**
     *  //向购物车添加物品
     * @param shoppingCart
     * @return
     */
    @PostMapping("add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        //获取用户id
        Long userId = BaseContext.get();
        //判断用户的购物车是否该购物项
        ShoppingCart one = shoppingCartService.lambdaQuery()
                .eq(ShoppingCart::getId, userId)
                .eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId())
                .eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId())
                .one();
        if (one!=null){
            //说明购物车有数据,需要修改操作
            one.setNumber(one.getNumber()+1);
            shoppingCartService.updateById(one);
            return R.success(one);
        }else {
            //说明购物车没有数据,需要添加操作
            shoppingCart.setUserId(userId);
            //设置数量默认为1
            shoppingCart.setNumber(1);
            //添加设置时间
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            return R.success(shoppingCart);
        }
    }




    /**
     * //清空购物车
     * @return
     */
    @DeleteMapping("clean")
    public R cleanShoping(){

        shoppingCartService.lambdaUpdate()
                .eq(ShoppingCart::getUserId,BaseContext.get())
                .remove();

        return R.success("删除成功");
    }




    /**
     * //移除购物车物品
     * @param shoppingCart
     * @return
     */
    @PostMapping("sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        //获取用户id
        long userId = BaseContext.get();
        //判断用户的购物车是否有购物项
        ShoppingCart one = shoppingCartService.lambdaQuery()
                .eq(ShoppingCart::getUserId, userId)
                .eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId())
                .eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId())
                .one();
        if (one.getNumber()>1){
            //说明需要修改操作,在one的基础上,将数量-1
            one.setNumber(one.getNumber()-1);
            shoppingCartService.updateById(one);
        }else {
            shoppingCartService.removeById(one.getId());
            one.setNumber(0);
        }
        return R.success(one);

    }
}
