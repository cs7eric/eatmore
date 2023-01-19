package com.cs7eric.eatmore.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cs7eric.eatmore.common.R;
import com.cs7eric.eatmore.dto.OrdersDto;
import com.cs7eric.eatmore.entity.OrderDetail;
import com.cs7eric.eatmore.entity.Orders;
import com.cs7eric.eatmore.service.OrderDetailService;
import com.cs7eric.eatmore.service.OrderService;
import com.cs7eric.eatmore.util.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;


    @PostMapping("/submit")
    public R<String> submit (@RequestBody Orders orders){

        log.info("orders:{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

    /**
     * 用户页面展示
     *
     * @param page     页面
     * @param pageSize  每页展示数据数量
     * @return {@link R}<{@link List}<{@link Orders}>>
     */
    @GetMapping("/userPage")
    public R<Page<OrdersDto>> page(int page, int pageSize){

        log.info("page{}",page);
        log.info("pageSize{}",pageSize);

        Page<Orders> pageInfo  = new Page<>(page, pageSize);

        Page<OrdersDto> dtoPage = orderService.pageWithDetail(page, pageSize);

        return R.success(dtoPage);
    }
}
