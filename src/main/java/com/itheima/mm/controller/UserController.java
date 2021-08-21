package com.itheima.mm.controller;

import com.itheima.framework.Controller;
import com.itheima.framework.RequestMapping;
import com.itheima.mm.constants.Constants;
import com.itheima.mm.entity.Result;
import com.itheima.mm.pojo.User;
import com.itheima.mm.service.UserService;
import com.itheima.mm.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 包名:com.itheima.mm.controller
 * @author Leevi
 * 日期2020-11-02  09:39
 */
@Controller
public class UserController {
    private UserService userService = new UserService();
    @RequestMapping("/user/login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //1. 接收请求参数
            User parameterUser = JsonUtils.parseJSON2Object(request, User.class);
            //2. 调用业务层的方法，校验用户名和密码
            User loginUser = userService.findUser(parameterUser);

            //保存登录状态!!!!将loginUser存储到session中
            request.getSession().setAttribute(Constants.USER_SESSION_KEY,loginUser);

            //没有出现异常，说明登录成功
            JsonUtils.printResult(response,new Result(true,"登录成功",loginUser));
        } catch (Exception e) {
            e.printStackTrace();
            //出现异常说明登录失败
            JsonUtils.printResult(response,new Result(false,e.getMessage()));
        }
    }

    @RequestMapping(value = "/user/logout")
    public void logout(HttpServletRequest request,HttpServletResponse response) throws IOException {
        try {
            //清除登录状态
            request.getSession().invalidate();

            JsonUtils.printResult(response,new Result(true,"退出登录成功"));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response,new Result(false,"退出登录失败"));
        }
    }
}
