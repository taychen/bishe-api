package com.bishe.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 响应实体
 *
 * @param <T>
 * @author chentay
 * @date 2019/01/16
 */
@Data
@ApiModel(value = "JsonResult", description = "数据响应实体")
public class JsonResult<T> {

    @ApiModelProperty(value = "code，响应码\n200：成功\n201：失败\n101：参数错误\n104：资源找不到，如设备不存在等等\n" +
            "105：资源已存在，如设备已存在等等", dataType = "Integer")
    private Integer code = 200;

    @ApiModelProperty(value = "message，消息提示", dataType = "String")
    private String message = "Success";

    @ApiModelProperty(value = "data，数据实体", dataType = "Object")
    private T data;
}