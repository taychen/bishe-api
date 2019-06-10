package com.bishe.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bishe.api.ExceptionHandle;
import com.bishe.aspect.entity.SysLog;
import com.bishe.aspect.service.SysLogService;
import com.bishe.common.util.WebLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * AOP 统一处理Web请求日志
 *
 * @author chentay
 * @date 2019/06/03
 */
@Slf4j
@Aspect
@Component
public class WebLogAspect {

  private final SysLogService sysLogService;

  private final ExceptionHandle exceptionHandle;

  @Autowired
  public WebLogAspect(SysLogService sysLogService, ExceptionHandle exceptionHandle) {
    this.sysLogService = sysLogService;
    this.exceptionHandle = exceptionHandle;
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

  /**
   * 环绕通知,环绕增强，相当于MethodInterceptor
   *
   * @param pjp pjp
   * @return {@link Object}
   */
  @Around("controllerLog()")
  public Object around(ProceedingJoinPoint pjp) throws Throwable {
    Object o;
    // 保存日志
    SysLog sysLog = new SysLog();
    try {
      o = pjp.proceed();
      WebLogUtils.getInstance().getLog(sysLog, pjp);
      log.info("响应结果：{}", o);
      JSONObject result = JSONObject.parseObject(JSON.toJSONString(o));
      log.info("请求结果：{}", result);
      sysLog.setResponse(result.getString("body"));

      // 调用service保存SysLog实体类到数据库
      sysLogService.save(sysLog);
      return o;
    } catch (Exception e) {
      e.printStackTrace();
      o = exceptionHandle.exceptionGet(e);
      WebLogUtils.getInstance().getLog(sysLog, pjp);
      sysLog.setResponse(o.toString());
      // 调用service保存SysLog实体类到数据库
      sysLogService.save(sysLog);
      return pjp.proceed();
    }
  }

}
