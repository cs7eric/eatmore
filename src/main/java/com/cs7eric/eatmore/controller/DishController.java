package com.cs7eric.eatmore.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cs7eric.eatmore.common.R;
import com.cs7eric.eatmore.dto.DishDto;
import com.cs7eric.eatmore.entity.Category;
import com.cs7eric.eatmore.entity.Dish;
import com.cs7eric.eatmore.entity.DishFlavor;
import com.cs7eric.eatmore.service.CategoryService;
import com.cs7eric.eatmore.service.DishFlavorService;
import com.cs7eric.eatmore.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *  菜品
 *
 * @author cs7eric
 * @date 2023/01/15
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     *  新增菜品
     *
     * @param dishDto  dto
     * @return {@link R}<{@link String}>
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){

        dishService.saveWithFlavor(dishDto);
        String key = "dish_" + dishDto.getCategoryId() + "_" + dishDto.getStatus();
        redisTemplate.delete(key);
        return R.success("新增成功");
    }

    /**
     * 分页查询
     *
     * @param page     当前分页
     * @param pageSize 每页数量
     * @param name     like 模糊查询关键词
     * @return {@link R}<{@link Page}>
     */
    @GetMapping("/page")
    public R<Page> page(int page, int  pageSize, String name){

        // 构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        // 条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Dish :: getName, name);
        queryWrapper.orderByDesc(Dish :: getCreateTime);

        // 执行分页查询
        dishService.page(pageInfo,queryWrapper);

        //  对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();

            //根据 ID 查询 分类对象
            Category category = categoryService.getById(categoryId);

            if (category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }


    /**
     * 通过id 查询 dish
     *
     * @param id id
     * @return {@link R}<{@link DishDto}>
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable("id") Long id){

        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);
    }

    /**
     * 通过id更新
     *
     * @param dishDto 菜dto
     * @return {@link R}<{@link String}>
     */
    @PutMapping
    public R<String> updateById(@RequestBody DishDto dishDto){

        dishService.updateByIdWithFlavor(dishDto);

        String key ="dish_" + dishDto.getCategoryId() + "_" + dishDto.getStatus();
        redisTemplate.delete(key);
        return R.success("修改成功");
    }


    /**
     *  更改状态
     *
     * @param ids    id
     * @param status 状态
     * @return {@link R}<{@link String}>
     */
    @PostMapping("/status/{status}")
    public R<String> status(Long[] ids, @PathVariable("status") Integer status){

        for (Long id : ids) {
            LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Dish::getId, id);
            updateWrapper.set(Dish::getStatus, status);
            dishService.update(updateWrapper);
        }

        return R.success("修改成功");
    }

    /**
     * 删除
     *
     * @param ids id
     * @return {@link R}<{@link String}>
     */
    @DeleteMapping
    public R<String> delete(Long[] ids){

        log.info("ids:{}", ids);
        dishService.deleteWithFlavor(ids);
        return R.success("删除成功");
    }


    /**
     * 查询 dish 信息
     *
     * @param dish 菜
     * @return {@link R}<{@link List}<{@link DishDto}>>
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){

        //  提前定义好 dishDtoList，用于接收 redis 获取的 value
        List<DishDto> dishDtoList = null;

        // 动态 构造 key
        // 要保证 key 的 唯一，我们动态构造key
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();

        log.info("{}", key);
        //从 redis 中 获取缓存数据（移动端使用 redis 缓存，将每个分类下查询的数据都放到缓存，避免重复查询，降低服务器压力）
        // 根据 key 获取 value
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);

        //判断 value 是否为空，就是看看 redis 中是否缓存了 该分类下的 数据
        if (dishDtoList != null) {

            // 有的话，直接返回
            return R.success(dishDtoList);
        }

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish :: getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish :: getStatus, 1);
        queryWrapper.orderByAsc(Dish :: getSort).orderByDesc(Dish :: getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor :: getDishId, item.getId());
            List<DishFlavor> dishFlavors = dishFlavorService.list(wrapper);
            dishDto.setFlavors(dishFlavors);
            return dishDto;
        }).collect(Collectors.toList());

        redisTemplate.opsForValue().set(key, dtoList, 30, TimeUnit.MINUTES);
        return R.success(dtoList);
    }

}
