package com.cs7eric.eatmore.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cs7eric.eatmore.common.R;
import com.cs7eric.eatmore.dto.DishDto;
import com.cs7eric.eatmore.entity.ShoppingCart;
import com.cs7eric.eatmore.service.ShoppingCartService;
import com.cs7eric.eatmore.util.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加 购物车
     *
     * @param shoppingCart 购物车
     * @return {@link R}<{@link DishDto}>
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){

        log.info("shoppingCart:{}",shoppingCart);
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart:: getUserId, currentId);

        if (dishId != null){
            // 添加到购物车里的是菜品
            queryWrapper.eq(ShoppingCart:: getDishId, dishId);
        } else {
            // 添加到购物车里的是 套餐
            queryWrapper.eq(ShoppingCart:: getSetmealId, shoppingCart.getSetmealId());
        }

        // 查询当前菜品 是否在购物车中
        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(queryWrapper);
        if (shoppingCartServiceOne != null){
            // 如果已经存在 就在原来的基础上加一
            Integer number = shoppingCartServiceOne.getNumber();
            shoppingCartServiceOne.setNumber(++number);
            shoppingCartService.updateById(shoppingCartServiceOne);
        } else {
            // 如果不存在，则添加到购物车，数量默认就是 1
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            shoppingCartServiceOne = shoppingCart;
        }
        return R.success(shoppingCartServiceOne);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){

        log.info("查看购物车...");

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return R.success(list);
    }

}
