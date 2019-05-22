package com.bishe.user.service;

import com.bishe.user.entity.SysUser;

/**
 * User Service
 *
 * @author chentay
 * @date 2019/05/22
 */
public interface UserService {

    /**
     * 获取用户数据
     *
     * @param username 用户名
     * @param password 密码
     * @return {@link SysUser}
     */
    SysUser loadUserByUsername(String username, String password);
}
