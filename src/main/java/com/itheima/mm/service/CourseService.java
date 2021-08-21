package com.itheima.mm.service;

import com.itheima.mm.dao.CatalogDao;
import com.itheima.mm.dao.CourseDao;
import com.itheima.mm.dao.QuestionDao;
import com.itheima.mm.dao.TagDao;
import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.pojo.Course;
import com.itheima.mm.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.itheima.mm.service
 *
 * @author Leevi
 * 日期2020-11-02  10:51
 */
public class CourseService {
    public void addCourse(Course course) throws Exception {
        SqlSession sqlSession = SqlSessionFactoryUtils.openSqlSession();
        CourseDao courseDao = sqlSession.getMapper(CourseDao.class);
        courseDao.add(course);

        SqlSessionFactoryUtils.commitAndClose(sqlSession);
    }

    public PageResult findByPage(QueryPageBean queryPageBean) throws Exception {
        SqlSession sqlSession = SqlSessionFactoryUtils.openSqlSession();
        CourseDao courseDao = sqlSession.getMapper(CourseDao.class);

        //1. 要分清楚客户端传入的数字到底是int还是String类型
        //2. mybatis框架在进行判断的时候，会将int类型的0当做null处理，所以我们要将请求参数中int类型的0转成String类型的0
        //先出QueryPageBean中的查询条件中的0和1
        Map map = queryPageBean.getQueryParams();
        if (map != null) {
            //判断status是否是空字符串
            if (!map.get("status").equals("")) {
                String status = (Integer) map.get("status") + "";
                map.put("status",status);
            }
        }

        //1. 查询总条数
        Long total = courseDao.findTotalCourse(queryPageBean);

        //2. 查询当前页数据集合
        List<Course> courseList = courseDao.findCourseListByPage(queryPageBean);

        SqlSessionFactoryUtils.commitAndClose(sqlSession);
        return new PageResult(total,courseList);
    }

    public void updateCourse(Course course) throws Exception {
        SqlSession sqlSession = SqlSessionFactoryUtils.openSqlSession();
        CourseDao courseDao = sqlSession.getMapper(CourseDao.class);

        courseDao.update(course);

        SqlSessionFactoryUtils.commitAndClose(sqlSession);
    }

    public void deleteById(Integer id) throws Exception {
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlSessionFactoryUtils.openSqlSession();
            CatalogDao catalogDao = sqlSession.getMapper(CatalogDao.class);
            //删除之前要先进行判断
            //1. 判断当前学科是否有关联的二级目录: 以当前学科的id到t_catalog表中查询二级目录的数量，如果数量为0表示没有关联的二级目录
            Long catalogCount = catalogDao.findCatalogCountByCourseId(id);
            if (catalogCount != 0) {
                //有关联的二级目录，不能删除
                throw new RuntimeException("有关联的二级目录，不能删除");
            }

            //2. 判断当前学科是否有关联的标签
            TagDao tagDao = sqlSession.getMapper(TagDao.class);
            Long tagCount = tagDao.findTagCountByCourseId(id);
            if (tagCount != 0) {
                //有关联的标签，不能删除
                throw new RuntimeException("有关联的标签，不能删除");
            }

            //3. 判断当前学科是否有关联的题目
            QuestionDao questionDao = sqlSession.getMapper(QuestionDao.class);
            Long questionCount = questionDao.findQuestionCountByCourseId(id);
            if (questionCount != 0) {
                //有关联的题目，不能删除
                throw new RuntimeException("有关联的题目，不能删除");
            }

            //可以删除，调用CourseDao的方法进行删除
            CourseDao courseDao = sqlSession.getMapper(CourseDao.class);
            courseDao.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            SqlSessionFactoryUtils.commitAndClose(sqlSession);
        }
    }
}
