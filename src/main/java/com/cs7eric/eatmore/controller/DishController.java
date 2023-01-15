package com.cs7eric.eatmore.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cs7eric.eatmore.common.R;
import com.cs7eric.eatmore.dto.DishDto;
import com.cs7eric.eatmore.entity.Category;
import com.cs7eric.eatmore.entity.Dish;
import com.cs7eric.eatmore.service.CategoryService;
import com.cs7eric.eatmore.service.DishFlavorService;
import com.cs7eric.eatmore.service.DishService;
import com.fasterxml.jackson.databind.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

}
