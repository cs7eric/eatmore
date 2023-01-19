package com.cs7eric.eatmore.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cs7eric.eatmore.entity.Orders;

public interface OrderService extends IService<Orders> {
    void submit(Orders orders);
}
