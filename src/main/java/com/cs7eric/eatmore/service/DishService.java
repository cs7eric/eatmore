package com.cs7eric.eatmore.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cs7eric.eatmore.dto.DishDto;
import com.cs7eric.eatmore.entity.Dish;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  dish service
 *
 * @author cs7eric
 * @date 2023/01/15
 */
@Service
public interface DishService extends IService<Dish> {
    /**
     * 保存 菜品
     *
     * @param dishDto 菜dto
     */
    void saveWithFlavor(DishDto dishDto);

    /**
     * 通过id 查询
     *
     * @param id id
     * @return {@link DishDto}
     */
    DishDto getByIdWithFlavor(Long id);

    /**
     *  通过id更新
     *
     * @param dishDto 菜dto
     */
    void updateByIdWithFlavor(DishDto dishDto);

    /**
     * 删除
     *
     * @param ids id
     */
    void deleteWithFlavor(Long[] ids);
}
