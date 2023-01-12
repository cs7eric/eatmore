package com.cs7eric.eatmore.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cs7eric.eatmore.common.R;
import com.cs7eric.eatmore.entity.Employee;
import com.cs7eric.eatmore.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 员工 controller
 *
 * @author cs7eric
 * @date 2023/01/12
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    /**
     * 员工登录
     *
     * @param request  请求
     * @param employee 员工
     * @return {@link R}<{@link Employee}>
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

        //  1.将页面提交的密码 password 进行 md5 加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 2.根据页面提交的用户名 username 查询数据库
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee :: getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(lambdaQueryWrapper);

        // 3.如果 没有查询到 则 返回登陆失败结果
        if(Objects.isNull(emp)){
            return R.error("登陆失败");
        }

        // 4. 密码比对，如果不一致则返回登陆失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("登陆失败");
        }

        // 5.查看员工状态，如果为禁用状态，则返回员工已禁用结果
        if(emp.getStatus() == 0){
            return R.error("员工被禁用");
        }

        // 6.登陆成功，将员工 ID 存入 session 并返回登陆成功结果
        request.getSession().setAttribute("employee",emp.getId());

        return R.success(emp);

    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){

        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
}
