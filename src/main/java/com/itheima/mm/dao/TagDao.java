package com.itheima.mm.dao;

/**
 * 包名:com.itheima.mm.dao
 *
 * @author Leevi
 * 日期2020-11-02  15:10
 */
public interface TagDao {
    Long findTagCountByCourseId(int courseId);
}
