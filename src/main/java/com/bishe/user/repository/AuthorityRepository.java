package com.bishe.user.repository;

import com.bishe.user.entity.SysAuthority;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Authority Repository
 *
 * @author chentay
 * @date 2019/05/23
 */
@Repository
public interface AuthorityRepository extends PagingAndSortingRepository<SysAuthority,Long> {

    /**
     * 根据权限名称获取权限信息
     *
     * @param name 名称
     * @return {@link Optional}
     */
    Optional<SysAuthority> findByName(String name);

    /**
     * 根据权限属性获取权限信息
     *
     * @param value 属性
     * @return {@link Optional}
     */
    Optional<SysAuthority> findByValue(String value);

}
