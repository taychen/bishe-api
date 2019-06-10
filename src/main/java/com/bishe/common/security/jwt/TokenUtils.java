package com.bishe.common.security.jwt;

import com.bishe.common.exception.TokenException;
import com.bishe.common.properties.ResultEnums;
import com.bishe.common.properties.RoleEnums;
import com.bishe.user.entity.SysRole;
import com.bishe.user.entity.SysUser;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * token处理辅助类
 *
 * @author chentay
 * @date 2019/04/13
 */
public class TokenUtils {

  private TokenUtils() {}

  private static class HolderClass {
    private static final TokenUtils INSTANCE = new TokenUtils();
  }

  public static TokenUtils getInstance() {
    return HolderClass.INSTANCE;
  }

  /**
   * 无权限操作
   *
   * @param request 请求
   */
  public SysUser isPermission(HttpServletRequest request) {
    SysUser userInfo = JwtUtils.getInstance().getUserInfo(request);
    if (userInfo == null) {
      throw new TokenException(ResultEnums.PASS_NO_AUTH);
    }
    return userInfo;
  }

  /**
   * 无超管权限
   *
   * @param request 请求
   */
  public SysUser isSuperAdmin(HttpServletRequest request) {
    SysUser userInfo = JwtUtils.getInstance().getUserInfo(request);
    if (userInfo == null) {
      throw new TokenException(ResultEnums.PASS_NO_AUTH);
    }
    Set<SysRole> roles = userInfo.getRoles();
    if (roles.size() > 0) {
      int count =
          (int)
              roles.stream()
                  .filter(sysRole -> sysRole.getValue().contains(RoleEnums.SUPER_ADMIN.getValue()))
                  .count();
      if (count <= 0) {
        throw new TokenException(ResultEnums.PASS_NO_AUTH);
      }
    } else {
      throw new TokenException(ResultEnums.PASS_NO_AUTH);
    }
    return userInfo;
  }
}
