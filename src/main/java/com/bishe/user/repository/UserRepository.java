package com.bishe.user.repository;

import com.bishe.user.entity.SysUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User Repository
 *
 * @author chentay
 * @date 2019/05/23
 */
@Repository
public interface UserRepository extends MongoRepository<SysUser,String> {

    /**
     * 获取用户数据
     *
     * @param username 用户名
     * @return {@link Optional}
     */
    Optional<SysUser> findByUsername(String username);

    /**
     * 获取用户数据
     *
     * @param userid 用户编号
     * @return {@link Optional}
     */
    Optional<SysUser> findByUserid(String userid);

}
