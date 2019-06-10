package com.bishe.aspect;

import com.bishe.aspect.annotation.PreAuthorize;
import com.bishe.aspect.entity.SysLog;
import com.bishe.aspect.service.SysLogService;
import com.bishe.common.exception.TokenException;
import com.bishe.common.properties.ResultEnums;
import com.bishe.common.security.jwt.JwtUtils;
import com.bishe.common.util.ResultUtils;
import com.bishe.common.util.WebLogUtils;
import com.bishe.user.entity.SysAuthority;
import com.bishe.user.entity.SysRole;
import com.bishe.user.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * AOP 统一处理Web请求日志
 *
 * @author chentay
 * @date 2019/06/03
 */
@Slf4j
@Aspect
@Component
public class PermissionAspect {

  private final SysLogService logService;

  @Autowired
  public PermissionAspect(SysLogService logService) {
    this.logService = logService;
  }

  /** 切入点描述 这个是controller包的切入点 */
  @Pointcut("execution(public * com.bishe.api.*.*.*(..))")
  public void controllerLog() {} // 签名，可以理解成这个切入点的一个名称

  /**
   * 在切入点的方法run之前要干的
   *
   * @param joinPoint joinPoint
   */
  @Before("controllerLog()")
  public void logBeforeController(JoinPoint joinPoint) {
    log.info("方法的返回值:{}", joinPoint);
    // 从切面织入点处通过反射机制获取织入点处的方法
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    // 获取切入点所在的方法
    Method method = signature.getMethod();
    // 获取操作
    PreAuthorize myPreAuthorize = method.getAnnotation(PreAuthorize.class);
    if (myPreAuthorize != null) {
      SysLog sysLog = new SysLog();
      WebLogUtils.getInstance().getLog(sysLog, joinPoint);
      String value = myPreAuthorize.value();
      log.info("PreAuthorize value:{}", value);
      //  获取RequestAttributes
      RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
      //  从获取RequestAttributes中获取HttpServletRequest的信息
      assert requestAttributes != null;
      HttpServletRequest request =
          (HttpServletRequest)
              requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
      assert request != null;
      SysUser user = JwtUtils.getInstance().getUserInfo(request);
      log.info("user: {}", user);
      Set<SysRole> roles = user.getRoles();
      if (roles.size() <= 0) {
        sysLog.setResponse(ResultUtils.getInstance().toJSONString(ResultEnums.PASS_NO_AUTH));
        logService.save(sysLog);
        throw new TokenException(ResultEnums.PASS_NO_AUTH);
      } else {
        Set<SysAuthority> authorities = new HashSet<>();
        for (SysRole role : roles) {
          authorities.addAll(role.getAuthorities());
        }
        if (authorities.size() <= 0) {
          sysLog.setResponse(ResultUtils.getInstance().toJSONString(ResultEnums.PASS_NO_AUTH));
          logService.save(sysLog);
          throw new TokenException(ResultEnums.PASS_NO_AUTH);
        } else {
          int count = (int) authorities.stream().filter(sysAuthority -> sysAuthority.getValue().contains(value)).count();
          if (count<=0){
            sysLog.setResponse(ResultUtils.getInstance().toJSONString(ResultEnums.PASS_NO_AUTH));
            logService.save(sysLog);
            throw new TokenException(ResultEnums.PASS_NO_AUTH);
          }
        }
      }
    }
  }

  /**
   * 处理完请求返回内容
   *
   * @param ret ret
   */
  @AfterReturning(returning = "ret", pointcut = "controllerLog()")
  public void doAfterReturning(JoinPoint ret) {}

  /**
   * 后置异常通知
   *
   * @param jp jp
   */
  @AfterThrowing("controllerLog()")
  public void customThrows(JoinPoint jp) {
    log.info("方法异常时执行.....");
    log.info("方法异常的返回值:{}", jp);
  }

  /**
   * 后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
   *
   * @param jp jp
   */
  @After("controllerLog()")
  public void after(JoinPoint jp) {
    log.info("方法的返回值:{}", jp);
  }
}
