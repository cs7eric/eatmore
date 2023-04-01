package com.cs7eric.eatmore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs7eric.eatmore.dto.SetmealDto;
import com.cs7eric.eatmore.entity.Setmeal;
import com.cs7eric.eatmore.entity.SetmealDish;
import com.cs7eric.eatmore.mapper.SetmealMapper;
import com.cs7eric.eatmore.service.SetmealDishService;
import com.cs7eric.eatmore.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {


    @Autowired
    private SetmealDishService setmealDishService;


    @Override
    @Transactional
    public void updateWithDishes(SetmealDto setmealDto) {

        //更新setmeal表的基本信息
        this.updateById(setmealDto);
        //清理当前套餐对应的菜品数据---setmeal_dish表的delete操作
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(queryWrapper);
        //添加当前提交过来的菜品数据---setmeal_dish表的insert操作
        List<SetmealDish> dishes = setmealDto.getSetmealDishes();
        dishes = dishes.stream().map((item)->{
            item.setId(IdWorker.getId());
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(dishes);
    }

    /**
     *
     *
     * @param id id
     * @return {@link SetmealDto}
     */
    @Override
    @Transactional
    public SetmealDto getByIdWithDishes(Long id) {
        //查询套餐基本信息，从setmeal表查询
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        //查询当前套餐对应的菜品信息，从setmeal_dish表查询
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
        List<SetmealDish> dishes = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(dishes);
        return setmealDto;
    }

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
