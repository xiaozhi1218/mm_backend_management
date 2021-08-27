package com.itheima.mm.dao;

import com.itheima.mm.pojo.Catalog;

import java.util.List;

/**
 * 包名:com.itheima.mm.dao
 *
 * @author Leevi
 * 日期2020-11-02  15:07
 */
public interface CatalogDao {
    Long findCatalogCountByCourseId(int courseId);

    /**
     * 查询某个学科的所有二级目录
     * @param courseId
     * @return
     */
    List<Catalog> findCatalogListByCourseId(int courseId);
}
