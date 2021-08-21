package com.itheima.mm.dao;

import com.itheima.mm.pojo.User;

/**
 * 包名:com.itheima.mm.dao
 *
 * @author Leevi
 * 日期2020-11-02  09:50
 */
public interface UserDao {
    User findUserByUsername(String username);
}
