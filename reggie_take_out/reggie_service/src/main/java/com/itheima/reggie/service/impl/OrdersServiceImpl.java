package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.dto.OrdersDto;
import com.itheima.reggie.mapper.OrdersMapper;
import com.itheima.reggie.pojo.*;
import com.itheima.reggie.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private ShoppingCartService shoppingCartService;

     //支付请求


    @Override
    @Transactional
    public void submitOrders(Orders orders) {
        //TODO 1. 查询一个基础的数据（用户User、地址AddressBook、订单ID）
        Long userId = BaseContext.get();
        User user = userService.getById(userId);//用户对象
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());//地址对象
        long orderId = IdWorker.getId();//订单id
        BigDecimal total = new BigDecimal(0);//总金额

        //TODO 2. 封装List<OrderDetail>对象的数据，   参考购物车 List<ShoppingCart>
        List<OrderDetail> orderDetailList = new ArrayList<>();
        //查询用户的购物车
        List<ShoppingCart> shoppingCartList = shoppingCartService.lambdaQuery()
                .eq(ShoppingCart::getUserId,userId)
                .list();
        for (ShoppingCart cart : shoppingCartList) {
            //创建一个OrderDetail
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);//订单id
            orderDetail.setNumber(cart.getNumber());//设置数量
            orderDetail.setDishFlavor(cart.getDishFlavor());//口味
            orderDetail.setDishId(cart.getDishId());//菜品id
            orderDetail.setSetmealId(cart.getSetmealId());//套餐id
            orderDetail.setName(cart.getName());//名称
            orderDetail.setImage(cart.getImage());//图片
            orderDetail.setAmount(cart.getAmount());//金额 单价
            BigDecimal price = orderDetail.getAmount().multiply(new BigDecimal(cart.getNumber()));
            total = total.add(price);
            //将orderdetail放入list集合中
            orderDetailList.add(orderDetail);
        }

        //TODO 3. 封装Order对象的数据
        orders.setOrderTime(LocalDateTime.now());//下单时间
        orders.setCheckoutTime(LocalDateTime.now());//支付时间
        orders.setStatus(2);//状态 订单状态 1待付款，2待派送，3已派送，4已完成，5已取消
        orders.setUserId(userId);//用户id
        orders.setNumber(String.valueOf(orderId));//订单编号
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());//收获人
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getDetail());
        orders.setAmount(total);// 总金额
        orders.setId(orderId);

        this.save(orders);//保存订单
        orderDetailService.saveBatch(orderDetailList);//保存订单项

        //TODO 4. 清空购物车
        shoppingCartService.lambdaUpdate()
                .eq(ShoppingCart::getUserId, userId)
                .remove();
    }


}
