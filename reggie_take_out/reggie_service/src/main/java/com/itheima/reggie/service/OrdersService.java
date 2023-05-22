package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.OrdersDto;
import com.itheima.reggie.pojo.Orders;

public interface OrdersService extends IService<Orders> {
    //支付请求
    void submitOrders(Orders orders);

}
