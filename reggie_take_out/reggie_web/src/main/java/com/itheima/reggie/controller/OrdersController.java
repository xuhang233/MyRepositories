package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.OrdersDto;
import com.itheima.reggie.pojo.Orders;
import com.itheima.reggie.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单管理
 */
@RestController
@RequestMapping("order")
public class OrdersController {


    @Autowired
    private OrdersService ordersService;


    //支付请求
    @PostMapping("submit")
    public R<String> submit(@RequestBody Orders orders){
        ordersService.submitOrders(orders);
        return R.success("支付成功");
    }

    //服务端订单明细1
    @GetMapping("page")
    public R<Page> page(Integer page,Integer pageSize){
        //创建一个分页对象
        Page<Orders> pageInfo = new Page(page, pageSize);
        //分页查方法
        ordersService.lambdaQuery()
                //分页查询
                .page(pageInfo);
        //响应数据
        return R.success(pageInfo);
    }

    //客户端订单明细
    @GetMapping("userPage")
    public R<Page> userPages(Integer page,Integer pageSize,Long userId){
        //创建一个分页对象
        Page<Orders> pageInfo = new Page(page, pageSize);
        //分页查方法
        ordersService.lambdaQuery()
                //分页查询
                .page(pageInfo);
        //响应数据
        return R.success(pageInfo);
    }

    //订单明细,修改状态
    @PutMapping
    public R putOrder(){
        return R.success(null);
    }
}
