package com.cs7eric.eatmore.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cs7eric.eatmore.entity.Category;

/**
 *
 *
 * @author cs7eric
 * @date 2023/01/14
 */
public interface CategoryService extends IService<Category> {
    void remove(Long ids);
}
