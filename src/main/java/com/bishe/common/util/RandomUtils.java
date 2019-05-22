package com.bishe.common.util;

import java.util.Random;

/**
 * 随机数辅助类
 *
 * @author chentay
 * @date 2019/01/16
 */
public class RandomUtils {

    private RandomUtils() {
    }

    private static class HolderClass {
        private final static RandomUtils INSTANCE = new RandomUtils();
    }

    public static RandomUtils getInstance() {
        return HolderClass.INSTANCE;
    }

    /**
     * 根据指定长度生成字母和数字的随机数
     * 0~9的ASCII为48~57
     * A~Z的ASCII为65~90
     * a~z的ASCII为97~122
     *
     * @param length 指定长度
     * @return {@link String}
     */
    public String createRandomCharData(int length) {
        StringBuilder sb = new StringBuilder();
        //随机用以下三个随机生成器
        Random rand = new Random();
        Random randData = new Random();
        int data;
        for (int i = 0; i < length; i++) {
            int index = rand.nextInt(3);
            // 目的是随机选择生成数字，大小写字母
            switch (index) {
                case 0:
                    // 仅仅会生成0~9
                    data = randData.nextInt(10);
                    sb.append(data);
                    break;
                case 1:
                    // 保证只会产生65~90之间的整数
                    data = randData.nextInt(26) + 65;
                    sb.append((char) data);
                    break;
                case 2:
                    // 保证只会产生97~122之间的整数
                    data = randData.nextInt(26) + 97;
                    sb.append((char) data);
                    break;
                default:
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 根据指定长度生成纯数字的随机数
     *
     * @param length 指定长度
     * @return {@link String}
     */
    public String createData(int length) {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 随机生成字母加数字的密码
     *
     * @param lengths 密码的位数
     * @return {@link String}
     */
    public String createStringRandom(int lengths) {
        StringBuilder val = new StringBuilder();
        Random random = new Random();
        // 参数lengths，表示生成几位随机数
        for (int i = 0; i < lengths; i++) {
            String strOrNum = random.nextInt(2) % 2 == 0 ? "str" : "num";
            // 随机输出是字母还是数字
            if ("str".equalsIgnoreCase(strOrNum)) {
                // 随机输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val.append((char) (random.nextInt(26) + temp));
            } else {
                val.append(String.valueOf(random.nextInt(10)));
            }
        }
        return val.toString();
    }
}

