package com.itheima.mm.dao;

import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.pojo.Question;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.itheima.mm.dao
 *
 * @author Leevi
 * 日期2020-11-02  15:13
 */
public interface QuestionDao {
    Long findQuestionCountByCourseId(int courseId);

    Long findBasicQuestionCount(QueryPageBean queryPageBean);


    List<Question> findBasicQuestionList(QueryPageBean queryPageBean);

    void add(Question question);

    void associationQuestionAndTag(Map parameterMap);

}
