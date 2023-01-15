package com.cs7eric.eatmore.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cs7eric.eatmore.common.R;
import com.cs7eric.eatmore.entity.Category;
import com.cs7eric.eatmore.service.CategoryService;
import com.cs7eric.eatmore.service.DishService;
import com.cs7eric.eatmore.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 类别控制器
 *
 * @author cs7eric
 * @date 2023/01/14
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;



    /**
     *  新增分类
     *
     * @param category 类别
     * @return {@link R}<{@link String}>
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("新增分类成功");
    }


    /**
     * 分页查询
     *
     * @param page     大小
     * @param pageSize 页面大小
     * @return {@link R}<{@link Page}>
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){

        Page<Category> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category :: getSort);
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }


    /**
     * 删除
     *
     * @param ids id
     * @return {@link R}<{@link String}>
     */
    @DeleteMapping
    public R<String> delete(Long ids){

        categoryService.remove(ids);

        return R.success("删除成功");
    }


    /**
     *  根据 ID 更新 信息
     *
     * @param category 类别
     * @return {@link R}<{@link String}>
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){

        categoryService.updateById(category);
        return R.success("修改信息成功");
    }

    /**
     *  根据条件查询分类数据
     *
     * @param category 类别
     * @return {@link R}<{@link List}<{@link Category}>>
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null, Category :: getType, category.getType());
        queryWrapper.orderByAsc(Category :: getSort).orderByDesc(Category :: getSort);
        List<Category> list = categoryService.list(queryWrapper);

        return  R.success(list);
    }

}
