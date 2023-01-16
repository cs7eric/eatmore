package com.cs7eric.eatmore.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cs7eric.eatmore.dto.SetmealDto;
import com.cs7eric.eatmore.entity.SetmealDish;
import org.springframework.stereotype.Service;

@Service
public interface SetmealDishService extends IService<SetmealDish> {
    void saveWithDish(SetmealDto setmealDto);
}
