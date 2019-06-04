package com.bishe.aspect.repository;

import com.bishe.aspect.entity.SysLog;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 操作日志仓储
 *
 * @author chentay
 * @date 2019/06/04
 */
public interface SysLogRepository extends PagingAndSortingRepository<SysLog,Long> {
}
