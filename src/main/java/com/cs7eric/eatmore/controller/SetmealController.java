package com.cs7eric.eatmore.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cs7eric.eatmore.common.R;
import com.cs7eric.eatmore.dto.SetmealDto;
import com.cs7eric.eatmore.entity.Category;
import com.cs7eric.eatmore.entity.Dish;
import com.cs7eric.eatmore.entity.Setmeal;
import com.cs7eric.eatmore.service.CategoryService;
import com.cs7eric.eatmore.service.DishService;
import com.cs7eric.eatmore.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishService dishService;

    /**
     * 新增套餐
     *
     * @param setmealDto setmeal dto
     * @return {@link R}<{@link String}>
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {

        log.info("setmealDto:{}", setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增成功");
    }


    /**
     * 页面展示 - 分页查询
     *
     * @param page     页面
     * @param pageSize 页面大小
     * @param name     名字
     * @return {@link R}<{@link Page}>
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        //分页构造器对象
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        //添加查询条件，根据name进行like模糊查询
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);

        //添加排序条件，根据更新时间降序排列
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        // 查询
        setmealService.page(pageInfo, queryWrapper);
        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");

        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            // 对象拷贝
            BeanUtils.copyProperties(item, setmealDto);
            // 分类 id
            Long categoryId = item.getCategoryId();
            // 查询 数据库
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                // 分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }


    /**
     * （批量）更改售卖状态
     *
     * @param status 状态
     * @param ids    id
     * @return {@link R}<{@link String}>
     */
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable("status") Integer status, Long[] ids) {

        log.info("status:{}", status);
        log.info("ids:{}", ids);

        for (Long id : ids) {
            LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Setmeal::getId, id);
            updateWrapper.set(Setmeal::getStatus, status);
            setmealService.update(updateWrapper);
        }
        return R.success("已停售");
    }

    @DeleteMapping
    public R<String> remove(@RequestParam("ids") List<Long> ids) {

        boolean key = setmealService.removeBatchWithDish(ids);
        if (!key) {
            return R.error("请将所选套餐停售后再删除");
        }
        return R.success("删除成功");
    }

    /**
     * 根据条件查询套餐数据
     *
     * @param setmeal setmeal
     * @return {@link R}<{@link List}<{@link Setmeal}>>
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal) {

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal :: getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal :: getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal :: getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }


    @GetMapping("/dish/{id}")
    public R<Dish> dish(@PathVariable("id") Long id){

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id != null, Dish :: getId, id);
        Dish dish = dishService.getOne(queryWrapper);
        return R.success(dish);
    }
}
