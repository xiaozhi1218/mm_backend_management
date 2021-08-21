package com.itheima.mm.dao;

import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.pojo.Course;

import java.util.List;

/**
 * 包名:com.itheima.mm.dao
 *
 * @author Leevi
 * 日期2020-11-02  10:53
 */
public interface CourseDao {
    void add(Course course);

    Long findTotalCourse(QueryPageBean queryPageBean);

    List<Course> findCourseListByPage(QueryPageBean queryPageBean);

    void update(Course course);

    void deleteById(Integer id);
}
