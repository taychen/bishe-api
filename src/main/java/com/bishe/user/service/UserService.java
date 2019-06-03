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
     * @param userId 用户编号
     * @param password 密码
     * @return {@link SysUser}
     */
    SysUser loadUserByUserId(String userId, String password);

    /**
     * 获取用户数据
     * @param userId 用户编号
     * @return {@link SysUser}
     */
    SysUser findByUserId(String userId);

    /**
     * 保存用户
     *
     * @param user 需要保存的用户
     * @return {@link SysUser}
     */
    SysUser save(SysUser user);
}
