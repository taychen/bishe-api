package com.bishe.aspect.service;

import com.bishe.aspect.entity.SysLog;

/**
 * 操作日志业务
 *
 * @author chentay
 * @date 2019/06/04
 */
public interface SysLogService {

    /**
     * 保存日志
     *
     * @param log 日志
     * @return {@link SysLog}
     */
    SysLog save(SysLog log);
}
