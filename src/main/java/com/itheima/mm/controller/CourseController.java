package com.itheima.mm.controller;

import com.itheima.framework.Controller;
import com.itheima.framework.RequestMapping;
import com.itheima.mm.constants.Constants;
import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.entity.Result;
import com.itheima.mm.pojo.Course;
import com.itheima.mm.pojo.User;
import com.itheima.mm.service.CourseService;
import com.itheima.mm.utils.DateUtils;
import com.itheima.mm.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 包名:com.itheima.mm.controller
 *
 * @author Leevi
 * 日期2020-11-02  10:28
 */
@Controller
public class CourseController {
    private CourseService courseService = new CourseService();

    @RequestMapping("/course/add")
    public void addCourse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //1. 获取请求参数
            Course course = JsonUtils.parseJSON2Object(request, Course.class);
            //2. 设置course的其它数据:createDate、userId、orderNo
            course.setCreateDate(DateUtils.parseDate2String(new Date()));
            course.setOrderNo(1);
            //从session中获取当前的用户
            User user = (User) request.getSession().getAttribute(Constants.USER_SESSION_KEY);
            course.setUserId(user.getId());

            //3. 调用业务层的方法保存学科course的信息
            courseService.addCourse(course);
            //添加学科成功
            JsonUtils.printResult(response,new Result(true,"添加学科成功"));
        } catch (Exception e) {
            e.printStackTrace();
            //添加学科失败
            JsonUtils.printResult(response,new Result(false,"添加学科失败"));
        }
    }

    @RequestMapping("/course/list")
    public void list(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //1. 获取请求参数
            QueryPageBean queryPageBean = JsonUtils.parseJSON2Object(request, QueryPageBean.class);
            //2. 调用业务层的方法，查询数据
            PageResult pageResult = courseService.findByPage(queryPageBean);
            //分页查询数据成功
            JsonUtils.printResult(response,new Result(true,"查询学科分页列表成功",pageResult));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response,new Result(false,"查询学科分页列表失败"));
        }
    }

    @RequestMapping("/course/update")
    public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //1. 获取请求参数
            Course course = JsonUtils.parseJSON2Object(request, Course.class);
            //2. 调用业务层的方法修改学科信息
            courseService.updateCourse(course);
            //修改成功
            JsonUtils.printResult(response,new Result(true,"修改学科信息成功"));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response,new Result(false,"修改学科信息失败"));
        }
    }

    @RequestMapping("/course/delete")
    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //1. 获取请求参数
            Integer id = Integer.valueOf(request.getParameter("id"));
            //2. 调用业务层的方法，根据id删除学科信息
            courseService.deleteById(id);
            //删除成功
            JsonUtils.printResult(response,new Result(true,"删除学科信息成功"));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response,new Result(false,e.getMessage()));
        }
    }

    @RequestMapping("/course/findAll")
    public void findAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //1. 获取请求参数
            Map parameterMap = JsonUtils.parseJSON2Object(request, Map.class);
            //2. 调用业务层的方法，查询所有学科
            List<Course> courseList = courseService.findAll(parameterMap);

            JsonUtils.printResult(response,new Result(true,"获取学科列表成功",courseList));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response,new Result(false,"获取学科列表失败"));
        }
    }
}
