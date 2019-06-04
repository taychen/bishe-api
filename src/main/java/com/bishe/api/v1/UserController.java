package com.bishe.api.v1;

import com.alibaba.fastjson.JSONObject;
import com.bishe.aspect.annotation.SysWebLog;
import com.bishe.common.config.CustomRedisConfig;
import com.bishe.common.properties.ResultEnums;
import com.bishe.common.security.crypto.password.PasswordEncoder;
import com.bishe.common.security.jwt.JwtUtils;
import com.bishe.common.util.AesCbcUtils;
import com.bishe.common.util.ResultUtils;
import com.bishe.common.validated.ValidatedGroups;
import com.bishe.user.entity.SysUser;
import com.bishe.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * User Api
 *
 * @author chentay
 * @date 2019/05/22
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/v1/user")
public class UserController {

  @Value("${aescbc.key}")
  private String key;

  @Value("${aescbc.iv}")
  private String iv;

  private final UserService userService;

  private final CustomRedisConfig redisConfig;

  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserController(
      UserService userService, CustomRedisConfig redisConfig, PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.redisConfig = redisConfig;
    this.passwordEncoder = passwordEncoder;

  }

  /**
   * 用户登录
   *
   * @param userId 用户编号
   * @param password 用户密码
   * @return {@link ResponseEntity}
   */
  @SysWebLog(value = "用户登录") // 这里添加了AOP的自定义注解
  @RequestMapping("/login")
  public ResponseEntity login(
      @NotBlank(message = "userId is empty")
          @Pattern(regexp = "^[0-9]{6,10}$", message = "格式错误，用户编号为6-10位数字")
          String userId,
      @NotBlank(message = "password is empty") String password) {
    log.info("before decrypt userId:{}, password:{}", userId, password);
    password = password.replace(" ", "+");
    this.initAecCbc();
    password = AesCbcUtils.getInstance().decrypt(password);
    if (password == null) {
      return ResponseEntity.ok(
          ResultUtils.getInstance().toJSONString(ResultEnums.USER_OR_PASS_ERROR));
    }
    log.info("after decrypt userId:{}, password:{}", userId, password);
    SysUser user = userService.loadUserByUserId(userId, password);

    if (user == null) {
      return ResponseEntity.ok(ResultUtils.getInstance().toJSONString(ResultEnums.USER_NOT_FOUND));
    }

    String token = redisConfig.getAccessToken(userId);
    if (StringUtils.isEmpty(token)) {
      long captchaExpires = JwtUtils.JWT_TTL / 1000;
      token = JwtUtils.getInstance().createJWT(JSONObject.toJSONString(user), JwtUtils.JWT_TTL);
      redisConfig.setAccessToken(userId, token, captchaExpires);
    }

    return ResponseEntity.ok(ResultUtils.getInstance().toJSONByToken(token));
  }

  /**
   * 用户注册
   *
   * @param user 用户
   * @return {@link ResponseEntity}
   */
  @SysWebLog(value = "用户注册")
  @PostMapping("/register")
  public ResponseEntity register(
      @Validated(value = ValidatedGroups.Default.class) @RequestBody SysUser user) {
    String userId = user.getUserId();
    String password = user.getPassword();
    this.initAecCbc();
    log.info("before decrypt userId:{}, password:{}", userId, password);
    String passwordAes = AesCbcUtils.getInstance().decrypt(password);
    if (passwordAes == null) {
      return ResponseEntity.ok(ResultUtils.getInstance().toJSON(ResultEnums.PARAM_PASS_NO_ENCRYPT));
    }
    log.info("after decrypt userId:{}, password:{}", userId, passwordAes);
    SysUser sysUser = userService.findByUserId(userId);
    if (sysUser != null) {
      return ResponseEntity.ok(ResultUtils.getInstance().toJSONString(ResultEnums.USER_EXIST));
    }
    user.setPassword(passwordEncoder.encode(passwordAes));
    sysUser = userService.save(user);
    return ResponseEntity.ok(ResultUtils.getInstance().toJSONString(ResultEnums.SUCCESS, sysUser));
  }

  private void initAecCbc(){
    AesCbcUtils.getInstance().setSKey(key);
    AesCbcUtils.getInstance().setIvParameter(iv);
  }
}
