package com.cs7eric.eatmore.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cs7eric.eatmore.common.R;
import com.cs7eric.eatmore.entity.Employee;
import com.cs7eric.eatmore.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 员工 controller
 *
 * @author cs7eric
 * @date 2023/01/12
 */
@Slf4j
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

    /**
     * 注销
     *
     * @param request 请求
     * @return {@link R}<{@link String}>
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){

        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工信息
     *
     * @param request  请求
     * @param employee 员工
     * @return {@link R}<{@link String}>
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("封装的信息：{}",employee);

        //设置初始密码  123456
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));


        // 获取当前登陆用户的 ID

        employeeService.save(employee);
        return R.success("新增员工成功！");
    }


    /**
     * 分页查询
     *
     * @param page     页面
     * @param pageSize 页面大小
     * @param name     名字
     * @return {@link R}<{@link Page}>
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize, name);

        // 构造分页构造器
        Page<Employee> pageInfo = new Page<>(page, pageSize);

        // 构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name) ,Employee :: getName, name);
        // 添加排序条件
        queryWrapper.orderByDesc(Employee :: getUpdateTime);

        // 执行查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 更新 员工信息
     *
     * @param employee 员工
     * @return {@link R}<{@link String}>
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee){
        log.info(employee.toString());
        Long empId = (Long) request.getSession().getAttribute("employee");

        employeeService.updateById(employee);
        return R.success("员工信息更改成功");
    }


    /**
     * 通过id查询 员工信息
     *
     * @param id id
     * @return {@link R}<{@link Employee}>
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable("id") Long id){

        Employee employee = employeeService.getById(id);
        if(employee != null){
            return R.success(employee);
        }
        return R.error("没有查询到员工信息");

    }



}
