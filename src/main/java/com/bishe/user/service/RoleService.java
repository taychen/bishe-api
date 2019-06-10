package com.bishe.user.service;

import com.bishe.user.entity.SysRole;

/**
 * Role Service
 *
 * @author chentay
 * @date 2019/06/10
 */
public interface RoleService {

    /**
     * 添加角色
     *
     * @param sysRole 角色
     * @return {@link SysRole}
     */
    SysRole addRole(SysRole sysRole);
}
