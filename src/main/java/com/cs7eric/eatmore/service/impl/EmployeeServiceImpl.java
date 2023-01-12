package com.cs7eric.eatmore.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs7eric.eatmore.entity.Employee;
import com.cs7eric.eatmore.mapper.EmployeeMapper;
import com.cs7eric.eatmore.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * 员工serviceImpl
 *
 * @author cs7eric
 * @date 2023/01/12
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
                                                            implements EmployeeService {


}
