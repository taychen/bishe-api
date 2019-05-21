package com.bishe.common.exception;

import com.bishe.common.properties.ResultEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义服务异常
 *
 * @author chentay
 * @date 2019/01/16
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ServerException extends RuntimeException{

    private Integer code;

    public ServerException(Integer code, String msg) {
        super(msg);
        this.setCode(code);
    }

    public ServerException(ResultEnums resultEnums) {
        super(resultEnums.getMessage());
        this.setCode(resultEnums.getCode());
    }
}