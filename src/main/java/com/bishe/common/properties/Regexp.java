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

    /**
     * 判断车牌号
     */
    public static final String VALIDATED_CAR = "^([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{1}(([A-HJ-Z]{1}[A-HJ-NP-Z0-9]{5})|([A-HJ-Z]{1}(([DF]{1}[A-HJ-NP-Z0-9]{1}[0-9]{4})|([0-9]{5}[DF]{1})))|([A-HJ-Z]{1}[A-D0-9]{1}[0-9]{3}警)))|([0-9]{6}使)|((([沪粤川云桂鄂陕蒙藏黑辽渝]{1}A)|鲁B|闽D|蒙E|蒙H)[0-9]{4}领)|(WJ[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼·•]{1}[0-9]{4}[TDSHBXJ0-9]{1})|([VKHBSLJNGCE]{1}[A-DJ-PR-TVY]{1}[0-9]{5})$";
}
