package com.bishe.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bishe.aspect.annotation.SysWebLog;
import com.bishe.aspect.entity.SysLog;
import com.bishe.aspect.service.SysLogService;
import com.bishe.common.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

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

  @Autowired
  public WebLogAspect(SysLogService sysLogService) {
    this.sysLogService = sysLogService;
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
  public void logBeforeController(JoinPoint joinPoint) {}

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
  }

  /**
   * 后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
   *
   * @param jp jp
   */
  @After("controllerLog()")
  public void after(JoinPoint jp) {}

  /**
   * 环绕通知,环绕增强，相当于MethodInterceptor
   *
   * @param pjp pjp
   * @return {@link Object}
   */
  @Around("controllerLog()")
  public Object around(ProceedingJoinPoint pjp) {
    try {
      // 处理完请求，返回内容
      log.info("方法的返回值:{}", pjp);
      // 保存日志
      SysLog sysLog = new SysLog();
      // 从切面织入点处通过反射机制获取织入点处的方法
      MethodSignature signature = (MethodSignature) pjp.getSignature();
      // 获取切入点所在的方法
      Method method = signature.getMethod();

      // 获取操作
      SysWebLog myLog = method.getAnnotation(SysWebLog.class);
      if (myLog != null) {
        String value = myLog.value();
        // 保存获取的操作
        sysLog.setOperation(value);
      }

      // 获取请求的类名
      String className = pjp.getTarget().getClass().getName();
      // 获取请求的方法名
      String methodName = method.getName();
      sysLog.setMethod(className + "." + methodName);

      // 请求的参数
      Object[] args = pjp.getArgs();
      // 将参数所在的数组转换成json
      String params = JSON.toJSONString(args);
      sysLog.setParams(params);

      //  获取RequestAttributes
      RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
      //  从获取RequestAttributes中获取HttpServletRequest的信息
      assert requestAttributes != null;
      HttpServletRequest request =
          (HttpServletRequest)
              requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);

      String ip = IpUtil.getIpAddr(request);
      sysLog.setIp(ip);

      Object o = pjp.proceed();
      JSONObject result = JSONObject.parseObject(JSON.toJSONString(o));
      log.info("请求结果：{}", result);
      sysLog.setResponse(result.getString("body"));

      // 调用service保存SysLog实体类到数据库
      sysLogService.save(sysLog);

      return o;
    } catch (Throwable e) {
      e.printStackTrace();
      return null;
    }
  }
}
