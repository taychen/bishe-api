package com.bishe.common.exception;

import com.bishe.common.properties.ResultEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义异常
 *
 * @author chentay
 * @date 2019/01/16
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class UserNotFoundException extends RuntimeException{

    private Integer code;

    public UserNotFoundException(ResultEnums resultEnums) {
        super(resultEnums.getMessage());
        this.setCode(resultEnums.getCode());
    }
}