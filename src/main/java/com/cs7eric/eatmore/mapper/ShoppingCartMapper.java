package com.cs7eric.eatmore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cs7eric.eatmore.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
