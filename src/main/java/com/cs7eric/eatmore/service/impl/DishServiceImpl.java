package com.cs7eric.eatmore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs7eric.eatmore.dto.DishDto;
import com.cs7eric.eatmore.entity.Dish;
import com.cs7eric.eatmore.entity.DishFlavor;
import com.cs7eric.eatmore.mapper.DishMapper;
import com.cs7eric.eatmore.service.DishFlavorService;
import com.cs7eric.eatmore.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品，同时保存对应的口味数据
     *
     * @param dishDto 菜dto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {

        // 保存菜品的基本信息到菜品表
        this.save(dishDto);

        Long dishId = dishDto.getId();

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        // 保存菜品口味数据到 口味表dish_flavor
        dishFlavorService.saveBatch(flavors);
        String key = "dish_*";
        redisTemplate.delete(key);
    }

    @Override
    @Transactional
    public DishDto getByIdWithFlavor(Long id) {

        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();

        BeanUtils.copyProperties(dish,dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor :: getDishId, id);
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }

    /**
     * 通过 id 更新
     *
     * @param dishDto 菜dto
     */
    @Override
    @Transactional
    public void updateByIdWithFlavor(DishDto dishDto) {

        // 更新 dish 的基本信息
        this.updateById(dishDto);

        // 清理当前菜品对应口味数据---dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor :: getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        //添加当前提交过来的口味数据---dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);

    }

    /**
     * 删除
     *
     * @param ids id
     */
    @Override
    @Transactional
    public void deleteWithFlavor(Long[] ids) {

        this.removeBatchByIds(Arrays.asList(ids));
        for (int i = 0; i < ids.length; i++) {
            LambdaQueryWrapper<DishFlavor> flavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            flavorLambdaQueryWrapper.eq(DishFlavor :: getDishId, ids[i]);
            dishFlavorService.remove(flavorLambdaQueryWrapper);
        }
        // 只清理该修改菜品分类下的缓存数据，精确清理，因为可能已经缓存了 好几个分类下的 数据
        String key = "dish_*" ;
        redisTemplate.delete(key);
    }
}
