package com.cs7eric.eatmore.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs7eric.eatmore.entity.DishFlavor;
import com.cs7eric.eatmore.mapper.DishFlavorMapper;
import com.cs7eric.eatmore.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
                                                            implements DishFlavorService {
}
