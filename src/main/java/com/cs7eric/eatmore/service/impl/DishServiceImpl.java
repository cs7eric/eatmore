package com.cs7eric.eatmore.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs7eric.eatmore.entity.Dish;
import com.cs7eric.eatmore.mapper.DishMapper;
import com.cs7eric.eatmore.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
