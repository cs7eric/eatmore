package com.cs7eric.eatmore.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs7eric.eatmore.dto.SetmealDto;
import com.cs7eric.eatmore.entity.SetmealDish;
import com.cs7eric.eatmore.mapper.SetmealDishMapper;
import com.cs7eric.eatmore.service.SetmealDishService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {

    @Override
    public void saveWithDish(SetmealDto setmealDto) {

        this.saveBatch(setmealDto.getSetmealDishes());


    }
}
