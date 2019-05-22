package com.bishe.user.service.impl;

import com.bishe.common.exception.ErrorException;
import com.bishe.common.exception.UserNotFoundException;
import com.bishe.common.properties.ResultEnums;
import com.bishe.common.security.crypto.password.PasswordEncoder;
import com.bishe.user.entity.SysUser;
import com.bishe.user.repository.UserRepository;
import com.bishe.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User Service implements
 *
 * @author chentay
 * @date 2019/05/22
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SysUser loadUserByUsername(String username, String password) {

        SysUser user = userRepository.findByUsername(username).orElse(null);

        if (user==null) {
            throw new UserNotFoundException(ResultEnums.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(password,user.getPassword())){
            throw new ErrorException(ResultEnums.USER_OR_PASS_ERROR);
        }

        return user;
    }
}
