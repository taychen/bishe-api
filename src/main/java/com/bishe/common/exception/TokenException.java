package com.bishe.common.exception;

import com.bishe.common.properties.ResultEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义错误异常
 *
 * @author chentay
 * @date 2019/01/16
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class TokenException extends RuntimeException{

    private Integer code;

    public TokenException(ResultEnums resultEnums) {
        super(resultEnums.getMessage());
        this.setCode(resultEnums.getCode());
    }
}