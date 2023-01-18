package com.cs7eric.eatmore.dto;

import com.cs7eric.eatmore.entity.Dish;
import com.cs7eric.eatmore.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜dto
 *
 * @author cs7eric
 * @date 2023/01/19
 */
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
