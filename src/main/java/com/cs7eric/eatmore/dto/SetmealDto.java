package com.cs7eric.eatmore.dto;

import com.cs7eric.eatmore.entity.Setmeal;
import com.cs7eric.eatmore.entity.SetmealDish;
import lombok.Data;
import java.util.List;

/**
 * setmeal dto
 *
 * @author cs7eric
 * @date 2023/01/16
 */
@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
