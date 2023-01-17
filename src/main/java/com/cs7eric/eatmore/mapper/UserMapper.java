package com.cs7eric.eatmore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cs7eric.eatmore.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
