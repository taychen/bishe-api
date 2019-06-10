package com.bishe.api;

import com.bishe.aspect.annotation.SysWebLog;
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

import javax.validation.ConstraintViolationException;
import java.util.Arrays;

/**
 * 自定义异常处理API
 *
 * @author chentay
 * @date 2019/01/16
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandle {

    /**
     * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中
     * @param e e
     * @return {@link String}
     */
    @SysWebLog("异常处理")
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public String exceptionGet(Exception e){

        if(e instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
            // 解析原错误信息，封装后返回，此处返回非法的字段名称，原始值，错误信息
            log.info("MethodArgumentNotValidException message:{}", exception.getMessage());
            return ResultUtils.getInstance().toJSON(ResultEnums.PARAM_ERROR.getCode(),
                    exception.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        }

        if(e instanceof HttpMessageNotReadableException){
            HttpMessageNotReadableException exception = (HttpMessageNotReadableException) e;
            // 解析原错误信息，封装后返回，此处返回非法的字段名称，原始值，错误信息
            log.info("HttpMessageNotReadableException message:{}", exception.getMessage());
            return ResultUtils.getInstance().toJSON(ResultEnums.PARAM_ERROR.getCode(), "参数无法正常解析");
        }

        if(e instanceof ConstraintViolationException){
            ConstraintViolationException exception = (ConstraintViolationException) e;
            log.info("ConstraintViolationException message:{}", exception.getMessage());
            String comma = ",";
            String message = exception.getMessage();
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

        if(e instanceof ServerException){
            ServerException exception = (ServerException) e;
            // 解析原错误信息，封装后返回，此处返回非法的字段名称，原始值，错误信息
            log.info("ServerException message:{}", exception.getMessage());
            return ResultUtils.getInstance().toJSON(exception.getCode(), exception.getMessage());
        }

        if(e instanceof UserNotFoundException){
            UserNotFoundException exception = (UserNotFoundException) e;
            // 解析原错误信息，封装后返回，此处返回非法的字段名称，原始值，错误信息
            log.info("UserNotFoundException message:{}", exception.getMessage());
            return ResultUtils.getInstance().toJSON(exception.getCode(), exception.getMessage());
        }

        if(e instanceof ErrorException){
            ErrorException exception = (ErrorException) e;
            // 解析原错误信息，封装后返回，此处返回非法的字段名称，原始值，错误信息
            log.info("ErrorException message:{}", exception.getMessage());
            return ResultUtils.getInstance().toJSON(exception.getCode(), exception.getMessage());
        }

        if(e instanceof TokenException){
            TokenException exception = (TokenException) e;
            // 解析原错误信息，封装后返回，此处返回非法的字段名称，原始值，错误信息
            log.info("TokenException message:{}", exception.getMessage());
            return ResultUtils.getInstance().toJSON(exception.getCode(), exception.getMessage());
        }

        log.error("【系统异常】{}",e.getMessage());
        String message = e.getMessage();
        if (!StringUtils.isEmpty(message)) {
            return ResultUtils.getInstance().toJSON(ResultEnums.SERVER_ERROR.getCode(), message);
        } else {
            return ResultUtils.getInstance().toJSONString(ResultEnums.SERVER_ERROR);
        }
    }
}
