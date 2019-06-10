package com.bishe.common.util;

import com.alibaba.fastjson.JSON;
import com.bishe.aspect.annotation.SysWebLog;
import com.bishe.aspect.entity.SysLog;
import com.bishe.common.security.jwt.JwtUtils;
import com.bishe.user.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Slf4j
public class WebLogUtils {

  private WebLogUtils() {}

  private static class HolderClass {
    private static final WebLogUtils INSTANCE = new WebLogUtils();
  }

  public static WebLogUtils getInstance() {
    return HolderClass.INSTANCE;
  }

  public void getLog(SysLog sysLog, JoinPoint pjp) {
    // 处理完请求，返回内容
    log.info("方法的返回值:{}", pjp);
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
    assert request != null;
    SysUser user = JwtUtils.getInstance().getUserInfo(request);
    if (user != null) {
      sysLog.setUserId(user.getUserId());
    }
    String ip = IpUtil.getIpAddr(request);
    sysLog.setIp(ip);
  }
}
