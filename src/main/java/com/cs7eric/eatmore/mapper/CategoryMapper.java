package com.cs7eric.eatmore.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cs7eric.eatmore.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * 类别
 *
 * @author cs7eric
 * @date 2023/01/14
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
