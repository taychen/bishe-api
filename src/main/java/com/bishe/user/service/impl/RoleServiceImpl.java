package com.bishe.user.service.impl;

import com.bishe.common.exception.ErrorException;
import com.bishe.common.properties.ResultEnums;
import com.bishe.user.entity.SysRole;
import com.bishe.user.repository.RoleRepository;
import com.bishe.user.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.RollbackException;

/**
 * Role Service implements
 *
 * @author chentay
 * @date 2019/05/22
 */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = RollbackException.class)
    @Override
    public SysRole addRole(SysRole sysRole) {
        SysRole role = roleRepository.findByName(sysRole.getName()).orElse(null);
        if (role!=null){
            throw new ErrorException(ResultEnums.ROLE_EXISTED);
        }
        role = roleRepository.findByValue(sysRole.getValue()).orElse(null);
        if (role!=null){
            throw new ErrorException(ResultEnums.ROLE_EXISTED);
        }
        role = roleRepository.save(sysRole);
        return role;
    }
}
