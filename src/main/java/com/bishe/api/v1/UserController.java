package com.bishe.api.v1;

import com.alibaba.fastjson.JSONObject;
import com.bishe.aspect.annotation.PreAuthorize;
import com.bishe.aspect.annotation.SysWebLog;
import com.bishe.common.config.CustomRedisConfig;
import com.bishe.common.properties.ResultEnums;
import com.bishe.common.security.crypto.password.PasswordEncoder;
import com.bishe.common.security.jwt.JwtUtils;
import com.bishe.common.util.AesCbcUtils;
import com.bishe.common.util.ResultUtils;
import com.bishe.common.validated.ValidatedGroups;
import com.bishe.user.dto.UserRequest;
import com.bishe.user.entity.SysAuthority;
import com.bishe.user.entity.SysRole;
import com.bishe.user.entity.SysUser;
import com.bishe.user.service.AuthorityService;
import com.bishe.user.service.RoleService;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

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

  private final RoleService roleService;

  private final AuthorityService authorityService;

  private final CustomRedisConfig redisConfig;

  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserController(
      UserService userService,
      RoleService roleService,
      AuthorityService authorityService,
      CustomRedisConfig redisConfig,
      PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.roleService = roleService;
    this.authorityService = authorityService;
    this.redisConfig = redisConfig;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * 初始化超级管理员
   *
   * @return {@link ResponseEntity}
   */
  @SysWebLog("初始化超级管理员")
  @RequestMapping("/init")
  public ResponseEntity init() {
    SysAuthority addAuth = new SysAuthority();
    addAuth.setName("添加");
    addAuth.setValue("Add");
    addAuth.setDescription("添加数据");
    addAuth = authorityService.addAuthority(addAuth);
    SysAuthority modifyAuth = new SysAuthority();
    modifyAuth.setName("修改");
    modifyAuth.setValue("Modify");
    modifyAuth.setDescription("修改数据");
    modifyAuth = authorityService.addAuthority(modifyAuth);
    SysAuthority searchAuth = new SysAuthority();
    searchAuth.setName("查询");
    searchAuth.setValue("Search");
    searchAuth.setDescription("查询数据");
    searchAuth = authorityService.addAuthority(searchAuth);
    SysAuthority deleteAuth = new SysAuthority();
    deleteAuth.setName("删除");
    deleteAuth.setValue("Delete");
    deleteAuth.setDescription("删除数据");
    deleteAuth = authorityService.addAuthority(deleteAuth);
    SysRole role = new SysRole();
    role.setName("超级管理员");
    role.setValue("ROLE_SUPER_ADMIN");
    role.setDescription("超级管理员，拥有所有权限");
    role.setAuthorities(new HashSet<>(Arrays.asList(addAuth, modifyAuth, searchAuth, deleteAuth)));
    roleService.addRole(role);
    SysUser user = new SysUser();
    user.setUsername("超级管理员");
    user.setUserId("1000000");
    user.setPassword(passwordEncoder.encode("superadmin"));
    user.setRoles(new HashSet<>(Collections.singletonList(role)));
    user = userService.save(user);
    return ResponseEntity.ok(ResultUtils.getInstance().toJSONString(ResultEnums.SUCCESS, user));
  }

  /**
   * 用户登录
   *
   * @param userId 用户编号
   * @param password 用户密码
   * @return {@link ResponseEntity}
   */
  @SysWebLog(value = "用户登录")
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
      @Validated(value = ValidatedGroups.Default.class) @RequestBody UserRequest user) {
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
    sysUser =
        userService.save(JSONObject.parseObject(JSONObject.toJSONString(user), SysUser.class));
    return ResponseEntity.ok(ResultUtils.getInstance().toJSONString(ResultEnums.SUCCESS, sysUser));
  }

  /**
   * 修改用户信息
   *
   * @param user 用户
   * @return {@link ResponseEntity}
   */
  @PreAuthorize("Modify")
  @SysWebLog(value = "修改用户信息")
  @PostMapping("/modify")
  public ResponseEntity modify(
      @Validated(value = ValidatedGroups.Modify.class) @RequestBody UserRequest user) {
    String userId = user.getUserId();
    String password = user.getPassword();
    SysUser sysUser = userService.findByUserId(userId);
    if (sysUser == null) {
      return ResponseEntity.ok(ResultUtils.getInstance().toJSONString(ResultEnums.USER_NOT_FOUND));
    }
    if (!StringUtils.isEmpty(password)) {
      this.initAecCbc();
      log.info("before decrypt password:{}", password);
      String passwordAes = AesCbcUtils.getInstance().decrypt(password);
      if (passwordAes == null) {
        return ResponseEntity.ok(
            ResultUtils.getInstance().toJSON(ResultEnums.PARAM_PASS_NO_ENCRYPT));
      }
      log.info("after decrypt password:{}", passwordAes);
      sysUser.setPassword(passwordEncoder.encode(passwordAes));
    }
    if (!StringUtils.isEmpty(user.getMobileCode())) {
      sysUser.setMobileCode(user.getMobileCode());
    }
    if (!StringUtils.isEmpty(user.getUsername())) {
      sysUser.setUsername(user.getUsername());
    }
    sysUser =
        userService.save(sysUser);
    return ResponseEntity.ok(ResultUtils.getInstance().toJSONString(ResultEnums.SUCCESS, sysUser));
  }

  private void initAecCbc() {
    AesCbcUtils.getInstance().setSKey(key);
    AesCbcUtils.getInstance().setIvParameter(iv);
  }
}
