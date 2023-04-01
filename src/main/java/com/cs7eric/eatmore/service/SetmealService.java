package com.cs7eric.eatmore.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cs7eric.eatmore.dto.SetmealDto;
import com.cs7eric.eatmore.entity.Setmeal;

import java.util.List;

/**
 * setmeal服务
 *
 * @author cs7eric
 * @date 2023/01/14
 */
public interface SetmealService extends IService<Setmeal> {
    /**
     * 保存 套餐
     *
     * @param setmealDto setmeal dto
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 批处理删除
     *
     * @param ids id
     * @return boolean
     */
    boolean removeBatchWithDish(List<Long> ids);

    SetmealDto getByIdWithDishes(Long id);

    void updateWithDishes(SetmealDto setmealDto);
}
