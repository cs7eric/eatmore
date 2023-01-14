package com.cs7eric.eatmore.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs7eric.eatmore.entity.Setmeal;
import com.cs7eric.eatmore.mapper.SetmealMapper;
import com.cs7eric.eatmore.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
