package com.bishe.user.service;

import com.bishe.user.entity.SysAuthority;
import com.bishe.user.entity.SysRole;

/**
 * Authority Service
 *
 * @author chentay
 * @date 2019/06/10
 */
public interface AuthorityService {

    /**
     * 添加权限
     *
     * @param sysAuthority 权限
     * @return {@link SysRole}
     */
    SysAuthority addAuthority(SysAuthority sysAuthority);


}
