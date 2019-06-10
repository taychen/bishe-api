package com.bishe.common.properties;

import lombok.Getter;

/**
 * 响应值枚举
 *
 * @author chentay
 * @date 2019/01/16
 */
@Getter
public enum ResultEnums {

    /**
     * 服务器错误
     **/
    SERVER_ERROR(500, "服务器错误"),

    SERVER_EXCEPTION(500, "服务器异常"),

    SERVER_TIME_OUT(500, "服务器超时"),

    AUTH_EXCEPTION(403, "授权异常"),

    PASS_NO_AUTH(403, "对不起，您无此权限操作"),

    USER_UPDATE_BASE_NO_AUTH(403,"当前操作只允许用户修改自身基本信息，不允许修改他人信息"),

    ACCESS_DENIED(401, "认证失败"),

    TOKEN_INVALID(401, "Token失效"),

    URL_NOT_FOUND(404, "Not Found"),

    SUCCESS(200, "成功"),

    FAILED(201, "失败"),

    PARAM_ERROR(101, "参数错误"),

    PARAM_IMG_ERROR(101, "请上传图片"),

    PARAM_PASS_NO_ENCRYPT(101,"密码未加密"),

    USER_OR_PASS_ERROR(101,"用户或密码错误"),

    USER_NOT_FOUND(104,"用户不存在"),

    USER_EXIST(105,"用户已存在"),

    USER_NICK_NAME_EXIST(105, "该用户昵称已存在"),

    USER_MOBILE_EXIST(105, "该手机号已注册"),

    USER_SUPER_EXIST(105,"超级管理员用户唯一，不允许重复添加"),

    ROLE_DEFAULT_NOT_ALLOWED_DELETE(401, "默认角色属性不允许修改/删除"),

    ROLE_USED_NOT_ALLOWED_MODIFY(401, "该角色已被注册使用，无法进行修改/删除操作"),

    ROLE_SUPER_ADD(101,"本服务只支持超级管理员属性添加"),

    ROLE_EXISTED(105, "角色已存在，请勿重复添加"),

    ROLE_NOT_FOUND(104, "角色不存在，请确认后再进行操作"),

    ROLES_NOT_FOUND(104, "查询不到任何角色信息"),

    AUTHORITY_EXISTED(105, "权限已存在，请勿重复添加"),;

    private int code;

    private String message;

    ResultEnums(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
