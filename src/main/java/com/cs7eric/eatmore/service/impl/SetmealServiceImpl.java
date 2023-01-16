package com.cs7eric.eatmore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs7eric.eatmore.dto.SetmealDto;
import com.cs7eric.eatmore.entity.Setmeal;
import com.cs7eric.eatmore.entity.SetmealDish;
import com.cs7eric.eatmore.mapper.SetmealMapper;
import com.cs7eric.eatmore.service.SetmealDishService;
import com.cs7eric.eatmore.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 保存套餐
     *
     * @param setmealDto setmeal dto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {

        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 批处理删除 套餐
     *
     * @param ids id
     * @return boolean
     */
    @Override
    @Transactional
    public boolean removeBatchWithDish(List<Long> ids) {

        // 判断所选 套餐状态
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal :: getId, ids)
                .eq(Setmeal :: getStatus, 1);
        long count = this.count(queryWrapper);
        if (count > 0){
            return false;
        }

        // 关系表删除
        LambdaQueryWrapper<SetmealDish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(SetmealDish :: getSetmealId, ids);
        setmealDishService.remove(dishLambdaQueryWrapper);

        this.removeBatchByIds(ids);
        return true;
    }
}
