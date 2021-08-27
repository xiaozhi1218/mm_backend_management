package com.itheima.mm.dao;

import com.itheima.mm.pojo.Tag;

import java.util.List;

/**
 * 包名:com.itheima.mm.dao
 *
 * @author Leevi
 * 日期2020-11-02  15:10
 */
public interface TagDao {
    Long findTagCountByCourseId(int courseId);

    List<Tag> findTagListByCourseId(int courseId);
}
