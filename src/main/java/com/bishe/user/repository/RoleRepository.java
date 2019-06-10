package com.bishe.user.repository;

import com.bishe.user.entity.SysRole;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Role Repository
 *
 * @author chentay
 * @date 2019/05/23
 */
@Repository
public interface RoleRepository extends PagingAndSortingRepository<SysRole,Long> {

    /**
     * 根据角色名称获取角色信息
     *
     * @param name 名称
     * @return {@link Optional}
     */
    Optional<SysRole> findByName(String name);

    /**
     * 根据角色属性获取角色信息
     *
     * @param value 属性
     * @return {@link Optional}
     */
    Optional<SysRole> findByValue(String value);
}
