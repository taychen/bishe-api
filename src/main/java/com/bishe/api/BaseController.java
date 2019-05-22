package com.bishe.api;

import com.bishe.common.exception.ErrorException;
import com.bishe.common.exception.ServerException;
import com.bishe.common.exception.TokenException;
import com.bishe.common.exception.UserNotFoundException;
import com.bishe.common.properties.ResultEnums;
import com.bishe.common.util.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

/**
 * 自定义异常处理API
 *
 * @author chentay
 * @date 2019/01/16
 */
@Slf4j
@ControllerAdvice
public class BaseController {

    /**
     * 自定义异常处理
     *
     * @param exception
     * @return TODO: 2018/07/17 参数未通过验证异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public String methodArgumentNotValidHandler(MethodArgumentNotValidException exception) {
        // 解析原错误信息，封装后返回，此处返回非法的字段名称，原始值，错误信息
        log.info("MethodArgumentNotValidException message:{}", exception.getMessage());
        return ResultUtils.getInstance().toJSON(ResultEnums.PARAM_ERROR.getCode(),
                exception.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * 自定义异常处理
     *
     * @param exception
     * @return TODO: 2018/07/17 无法解析参数异常
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public String httpMessageNotReadableHandler(HttpMessageNotReadableException exception) {
        log.info("HttpMessageNotReadableException message:{}", exception.getMessage());
        return ResultUtils.getInstance().toJSON(ResultEnums.PARAM_ERROR.getCode(), "参数无法正常解析");
    }

    /**
     * 非json参数校验异常处理
     *
     * @param e 异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public String constraintViolationHandler(ConstraintViolationException e) {
        log.info("ConstraintViolationException message:{}", e.getMessage());
        String comma = ",";
        String message = e.getMessage();
        final String[] result = {""};
        if (message.contains(comma)) {
            String[] strings = message.split(comma);
            Arrays.stream(strings).forEach(s -> result[0] += s.substring(s.indexOf(":") + 2) + ", ");
            result[0] = result[0].substring(0, result[0].length() - 2);
        } else {
            result[0] = message.substring(message.indexOf(":") + 2);
        }
        return ResultUtils.getInstance().toJSON(ResultEnums.PARAM_ERROR.getCode(), result[0]);
    }

    /**
     * 自定义异常处理
     *
     * @param e
     * @return {@link String}
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public String exceptionHandler(Exception e) {
        String message = e.getMessage();
        if (!StringUtils.isEmpty(message)) {
            return ResultUtils.getInstance().toJSON(ResultEnums.SERVER_ERROR.getCode(), message);
        } else {
            return ResultUtils.getInstance().toJSONString(ResultEnums.SERVER_ERROR);
        }

    }

    /**
     * 服务异常处理
     *
     * @param e
     * @return {@link String}
     * @date 2018/07/17
     */
    @ExceptionHandler(ServerException.class)
    @ResponseBody
    public String serverExceptionHandler(ServerException e, HttpServletResponse response) {
        response.setStatus(e.getCode());
        return ResultUtils.getInstance().toJSON(e.getCode(), e.getMessage());
    }

    /**
     * 服务异常处理
     *
     * @param e
     * @return {@link String}
     * @date 2018/07/17
     */
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public String userNotFoundExceptionHandler(UserNotFoundException e) {
        return ResultUtils.getInstance().toJSON(e.getCode(), e.getMessage());
    }

    /**
     * 服务异常处理
     *
     * @param e
     * @return {@link String}
     * @date 2018/07/17
     */
    @ExceptionHandler(ErrorException.class)
    @ResponseBody
    public String errorExceptionHandler(ErrorException e) {
        return ResultUtils.getInstance().toJSON(e.getCode(), e.getMessage());
    }

    /**
     * token异常处理
     *
     * @param e
     * @return {@link String}
     * @date 2018/07/17
     */
    @ExceptionHandler(TokenException.class)
    @ResponseBody
    public String tokenExceptionHandler(TokenException e, HttpServletResponse response) {
        response.setStatus(e.getCode());
        return ResultUtils.getInstance().toJSON(e.getCode(), e.getMessage());
    }

    /**
     * 短信客户端异常处理
     *
     * @param e
     * @return {@link String}
     * @date 2018/07/17
     */
    @ExceptionHandler(SocketTimeoutException.class)
    @ResponseBody
    public String socketTimeoutExceptionHandler(SocketTimeoutException e) {
        log.info("SocketTimeoutException:{}", e);
        return ResultUtils.getInstance().toJSON(ResultEnums.SERVER_TIME_OUT.getCode(), e.getMessage());
    }
}
