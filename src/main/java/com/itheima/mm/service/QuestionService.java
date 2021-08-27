package com.itheima.mm.service;

import com.itheima.mm.dao.QuestionDao;
import com.itheima.mm.dao.QuestionItemDao;
import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.pojo.Question;
import com.itheima.mm.pojo.QuestionItem;
import com.itheima.mm.pojo.Tag;
import com.itheima.mm.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包名:com.itheima.mm.service
 *
 * @author Leevi
 * 日期2020-11-04  09:38
 */
public class QuestionService {
    public PageResult findBasicQuestionListByPage(QueryPageBean queryPageBean) throws Exception {
        SqlSession sqlSession = SqlSessionFactoryUtils.openSqlSession();
        QuestionDao questionDao = sqlSession.getMapper(QuestionDao.class);
        //1. 查询总条数
        Long basicCount = questionDao.findBasicQuestionCount(queryPageBean);

        //2. 查询当前页的基础试题集合
        List<Question> basicList = questionDao.findBasicQuestionList(queryPageBean);

        SqlSessionFactoryUtils.commitAndClose(sqlSession);
        return new PageResult(basicCount,basicList);
    }

    public void add(Question question){
        //这三步需要使用事务
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlSessionFactoryUtils.openSqlSession();
            QuestionDao questionDao = sqlSession.getMapper(QuestionDao.class);
            //1. 将题目自身的。信息存储到t_question表
            questionDao.add(question);

            //2. 将题目的选项信息存储到t_question_item表
            QuestionItemDao questionItemDao = sqlSession.getMapper(QuestionItemDao.class);
            //获取题目选项列表
            List<QuestionItem> questionItemList = question.getQuestionItemList();
            if (questionItemList != null) {
                for (QuestionItem questionItem : questionItemList) {
                    //遍历出每一个题目选项
                    //添加之前，设置每一个选项的所属试题的id(questionId)
                    questionItem.setQuestionId(question.getId());
                    //添加每一个选项
                    questionItemDao.add(questionItem);
                }
            }

            //3. 关联题目和tag(标签)
            //获取该题目的标签列表
            List<Tag> tagList = question.getTagList();
            //遍历出每一个标签
            if (tagList != null) {
                for (Tag tag : tagList) {
                    //封装要存储到数据库的数据
                    Map parameterMap = new HashMap();
                    parameterMap.put("questionId", question.getId());
                    parameterMap.put("tagId", tag.getId());
                    questionDao.associationQuestionAndTag(parameterMap);
                }
            }

            //提交
            SqlSessionFactoryUtils.commitAndClose(sqlSession);
        } catch (Exception e) {
            e.printStackTrace();
            //回滚
            SqlSessionFactoryUtils.rollbackAndClose(sqlSession);
            throw new RuntimeException(e.getMessage());
        }
    }
}
