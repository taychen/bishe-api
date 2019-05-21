package com.bishe.common.properties;

import lombok.Getter;

/**
 * 默认角色属性
 *
 * @author chentay
 * @date 2019/01/22
 */
@Getter
public enum RoleEnums {

    /**
     * 超级管理员
     */
    SUPER_ADMIN("SUPER_ADMIN","超级管理员"),
    /**
     * 管理员
     */
    ADMIN("ADMIN","管理员"),

    TEACHER("TEACHER","教师"),

    STUDENT("STUDENT","学生"),

    /**
     * 普通用户
     */
    USER("USER","普通用户");

    /**
     * 属性
     */
    private String value;

    /**
     * 名称
     */
    private String name;

    RoleEnums(String value, String name) {
        this.value = value;
        this.name = name;
    }
}
