package com.cs7eric.eatmore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cs7eric.eatmore.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * 员工映射器
 *
 * @author cs7eric
 * @date 2023/01/12
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
