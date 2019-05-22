package com.bishe.common.util;

import lombok.Data;
import org.springframework.util.Base64Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理
 * 对原始数据进行AES加密后，在进行Base64编码转化；
 * 正确
 *
 * @author chentay
 * @date 2019/01/16
 */
@Data
public class AesCbcUtils {

    /**
     * 已确认
     * 加密用的Key 可以用26个字母和数字组成
     * 此处使用AES-128-CBC加密模式，key需要为16位。
     **/
    private String sKey;
    private String ivParameter;
    private static String encodingFormat = "utf-8";

    private AesCbcUtils() {

    }

    private static class HolderClass {
        private final static AesCbcUtils INSTANCE = new AesCbcUtils();
    }

    public static AesCbcUtils getInstance() {
        return HolderClass.INSTANCE;
    }

    /**
     * 加密
     *
     * @param sSrc 待加密字符串
     * @return {@link String}
     */
    public String encrypt(String sSrc) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] raw = sKey.getBytes(StandardCharsets.US_ASCII);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            //使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes(encodingFormat));
            // 此处使用BASE64做转码。
            return new String(Base64Utils.encode(encrypted));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException |
                InvalidKeyException | IllegalBlockSizeException | UnsupportedEncodingException | BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密
     *
     * @param sSrc 待解密字符串
     * @return {@link String}
     */
    public String decrypt(String sSrc) {
        try {
            byte[] raw = sKey.getBytes(StandardCharsets.US_ASCII);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            // 先用base64解密
            byte[] encryptedDec = Base64Utils.decode(sSrc.getBytes());
            byte[] original = cipher.doFinal(encryptedDec);
            return new String(original, encodingFormat);
        } catch (Exception ex) {
            return null;
        }
    }
}
