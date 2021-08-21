package com.itheima.mm.service;

import com.itheima.mm.dao.UserDao;
import com.itheima.mm.pojo.User;
import com.itheima.mm.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;

/**
 * 包名:com.itheima.mm.service
 *
 * @author Leevi
 * 日期2020-11-02  09:48
 */
public class UserService {
    public User findUser(User parameterUser) throws Exception {
        //1. 判断用户名是否正确: 调用dao层的方法，根据用户名查找用户
        SqlSession sqlSession = SqlSessionFactoryUtils.openSqlSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        User loginUser = userDao.findUserByUsername(parameterUser.getUsername());

        SqlSessionFactoryUtils.commitAndClose(sqlSession);
        if (loginUser != null) {
            //用户名正确
            //2. 判断密码是否正确 : 使用parameterUser的密码和loginUser的密码进行校验
            if (parameterUser.getPassword().equals(loginUser.getPassword())) {
                //密码正确
                return loginUser;
            }else {
                //密码错误
                throw new RuntimeException("密码错误");
            }
        }else {
            //用户名错误
            throw new RuntimeException("用户名错误");
        }
    }
}
