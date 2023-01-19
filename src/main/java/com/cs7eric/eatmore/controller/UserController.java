package com.cs7eric.eatmore.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cs7eric.eatmore.common.R;
import com.cs7eric.eatmore.entity.User;
import com.cs7eric.eatmore.service.UserService;
import com.cs7eric.eatmore.util.BaseContext;
import com.cs7eric.eatmore.util.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     *  发送 验证码
     *
     * @param user    用户
     * @param session 会话
     * @return {@link R}<{@link String}>
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){

        // 获取手机号
        String phone = user.getPhone();

        if(StringUtils.isNotEmpty(phone)){

            // 生成 随机的 4位验证码
            Integer code = ValidateCodeUtils.generateValidateCode(4);
            log.info("code:{}", code);

            // 调用 阿里提供的短信服务 API 发送验证码
            //SMSUtils.sendMessage("eatmore","",phone,code);

            // 将生成的验证码 存到 Session
            session.setAttribute(phone,code);

            return R.success("短信发送成功");
        }
        return R.error("短信发送失败");
    }

    /**
     * 用户登录
     *
     * @param map     地图
     * @param session 会话
     * @return {@link R}<{@link User}>
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){

        // 获取手机号
        String phone = map.get("phone").toString();

        // 获取验证码
        String code = map.get("code").toString();

        // 从 session 中 获取 验证码
//        Object codeInSession = session.getAttribute(phone);

        //进行 验证码的比对
        if(code != null ){
//            if(codeInSession != null && codeInSession.equals(code)){

            // 比对 成功 -> 登陆成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User :: getPhone, phone);
            User user = userService.getOne(queryWrapper);

            if( user == null){
                // 判断当前用户是否为新用户，如果 是新用户 就自动完成注册
                User newUser = new User();
                newUser.setPhone(phone);
                newUser.setStatus(1);
                userService.save(newUser);
            }
            queryWrapper.eq(User :: getPhone, phone);
            User one = userService.getOne(queryWrapper);
            session.setAttribute("user" , one.getId());
            return R.success(one);
        }
        return R.error("登陆失败");
    }


    /**
     *  客户端退出登录
     *
     * @return {@link R}<{@link String}>
     */
    @PostMapping("/loginout")
    public R<String> logout(){

        BaseContext.removeCurrentId();
        return R.success("退出成功");
    }
}
