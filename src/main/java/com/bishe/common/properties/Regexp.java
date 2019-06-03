package com.bishe.common.properties;

/**
 * 项目常用正则表达式
 *
 * @author chentay
 * @date 2019/01/23
 */
public class Regexp {

    /**
     * 判断中文输入并限定字数
     */
    public static final String REGEXP_NAME = "^[\\u4e00-\\u9fa5]{2,20}$";

    /**
     * 判断非空、非字母，非数字
     */
    public static final String REGEXP_DESC = "^[^\\w\\s]{10,300}$";

    /**
     * 判断手机号
     */
    public static final String VALIDATED_MOBILE_CODE= "^([1][3-9][0-9]{9})$";

    /**
     * 判断邮箱
     */
    public static final String VALIDATED_EMAIL = "^([a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6})$";

    /**
     * 判断number
     */
    public static final String VALIDATED_NUMBER = "^[0-9]{0,10}$";

    /**
     * 简单检验时间戳
     */
    public static final String VALIDATED_TIMESTAMP = "^[0-9]{10}$";

    /**
     * 判断是否为英文
     */
    public static final String VALIDATED_EN = "^([a-zA-Z]|[a-zA-Z_a-zA-Z])+$";

    public static final String VALIDATED_EN_NUM = "^(?![0-9])([a-zA-Z]|[a-zA-Z0-9_])+$";

}
