package com.bishe.aspect.service.impl;

import com.bishe.aspect.entity.SysLog;
import com.bishe.aspect.repository.SysLogRepository;
import com.bishe.aspect.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.RollbackException;

/**
 * 操作日志业务实现
 *
 * @author chentay
 * @date 2019/06/04
 */
@Slf4j
@Service
public class SysLogServiceImpl implements SysLogService {

    private final SysLogRepository logRepository;

    @Autowired
    public SysLogServiceImpl(SysLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = RollbackException.class)
    @Override
    public SysLog save(SysLog log) {
        return logRepository.save(log);
    }
}
