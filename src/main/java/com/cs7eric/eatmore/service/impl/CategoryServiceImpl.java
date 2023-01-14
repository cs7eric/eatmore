package com.cs7eric.eatmore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs7eric.eatmore.common.CustomException;
import com.cs7eric.eatmore.entity.Category;
import com.cs7eric.eatmore.entity.Dish;
import com.cs7eric.eatmore.entity.Setmeal;
import com.cs7eric.eatmore.mapper.CategoryMapper;
import com.cs7eric.eatmore.service.CategoryService;
import com.cs7eric.eatmore.service.DishService;
import com.cs7eric.eatmore.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 类别服务impl
 *
 * @author cs7eric
 * @date 2023/01/14
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
                                                        implements CategoryService {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private DishService dishService;


    /**
     *  根据id删除分类，删除之前需要进行判断
     *
     * @param ids id
     */
    @Override
    public void remove(Long ids) {

        //  添加查询条件，根据分类id进行查询
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal :: getCategoryId, ids);
        long setmealCount = setmealService.count(setmealLambdaQueryWrapper);
        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        if (setmealCount > 0){

            //已经关联菜品，抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }

        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        dishLambdaQueryWrapper.eq(Dish :: getCategoryId, ids);
        long dishCount = dishService.count(dishLambdaQueryWrapper);
        if (dishCount > 0){
            //已经关联套餐，抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        //正常删除
        super.removeById(ids);
    }
}
