package com.itheima.mm.dao;

/**
 * 包名:com.itheima.mm.dao
 *
 * @author Leevi
 * 日期2020-11-02  15:13
 */
public interface QuestionDao {
    Long findQuestionCountByCourseId(int courseId);
}
