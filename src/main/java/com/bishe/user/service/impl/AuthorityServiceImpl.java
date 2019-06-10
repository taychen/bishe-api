package com.bishe.user.service.impl;

import com.bishe.common.exception.ErrorException;
import com.bishe.common.properties.ResultEnums;
import com.bishe.user.entity.SysAuthority;
import com.bishe.user.repository.AuthorityRepository;
import com.bishe.user.service.AuthorityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.RollbackException;

/**
 * Authority Service implements
 *
 * @author chentay
 * @date 2019/05/22
 */
@Slf4j
@Service
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = RollbackException.class)
    @Override
    public SysAuthority addAuthority(SysAuthority sysAuthority) {
        SysAuthority authority = authorityRepository.findByName(sysAuthority.getName()).orElse(null);
        if (authority!=null){
            throw new ErrorException(ResultEnums.AUTHORITY_EXISTED);
        }
        authority = authorityRepository.findByValue(sysAuthority.getValue()).orElse(null);
        if (authority!=null){
            throw new ErrorException(ResultEnums.AUTHORITY_EXISTED);
        }
        authority = authorityRepository.save(sysAuthority);
        return authority;
    }
}
