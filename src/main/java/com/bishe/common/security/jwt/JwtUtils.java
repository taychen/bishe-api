package com.bishe.common.security.jwt;

import com.alibaba.fastjson.JSONObject;
import com.bishe.common.exception.TokenException;
import com.bishe.common.properties.ResultEnums;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * JwtUtils
 *
 * @author chentay
 * @date 2019/03/19
 */
@SuppressWarnings("all")
@Slf4j
public class JwtUtils {

    private static String JWT_ID = UUID.randomUUID().toString();

    /**
     * 加密密文
     */
    private static String JWT_SECRET = "Hct6ia515FC37v0Xl8796556220wAe74";
    /**
     * 时间:7天
     */
    public static int JWT_TTL = 60 * 60 * 24 * 7 * 1000;

    private JwtUtils() {

    }

    private static class HolderClass {
        private final static JwtUtils INSTANCE = new JwtUtils();
    }

    public static JwtUtils getInstance() {
        return HolderClass.INSTANCE;
    }

    /**
     * 由字符串生成加密key
     *
     * @return
     */
    private SecretKey generalKey() {
        String stringKey = JWT_SECRET;

        // 本地的密码解码
        byte[] encodedKey = Base64.decodeBase64(stringKey);

        // 根据给定的字节数组使用AES加密算法构造一个密钥

        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    /**
     * 创建jwt
     *
     * @param id
     * @param issuer
     * @param subject
     * @param ttlMillis
     * @return
     */
    public String createJWT(String subject, long ttlMillis) {

        // 指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", JWT_ID);

        // 生成签名的时候使用的秘钥secret，切记这个秘钥不能外露哦。它就是你服务端的私钥，在任何场景都不应该流露出去。
        // 一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
        SecretKey key = generalKey();

        // 下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder() // 这里其实就是new一个JwtBuilder，设置jwt的body
                .setClaims(claims)          // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setId(JWT_ID)                  // 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setIssuedAt(now)           // iat: jwt的签发时间
                .setIssuer("jwt")          // issuer：jwt签发人
                .setSubject(subject)        // sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .signWith(signatureAlgorithm, key); // 设置签名使用的签名算法和签名使用的秘钥

        // 设置过期时间
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * 解密jwt
     *
     * @param jwt
     * @return
     */
    public Claims parseJWT(String jwt) {
        SecretKey key = generalKey();  //   签名秘钥，和生成的签名的秘钥一模一样
        Claims claims = Jwts.parser()  //   得到DefaultJwtParser
                .setSigningKey(key)                 //  设置签名的秘钥
                .parseClaimsJws(jwt).getBody();     //  设置需要解析的jwt
        long exp = claims.getExpiration().getTime() / 1000;
        //  判断是否失效
        long currentTime = Instant.now().toEpochMilli() / 1000;
        if (exp <= currentTime) {
            throw new TokenException(ResultEnums.TOKEN_INVALID);
        }
        return claims;
    }

    /**
     * 解析token得到用户数据
     *
     * @param request 请求
     * @return {@link UserInfoEntity}
     */
//    public UserInfoEntity getUserInfo(HttpServletRequest request){
//        String accessToken = request.getHeader("Authorization").split(" ")[1];
//        Claims claims = parseJWT(accessToken);
//        if (claims!=null) {
//            UserInfoEntity userInfo = JSONObject.parseObject(claims.getSubject(),UserInfoEntity.class);
//            return userInfo;
//        }else {
//            return null;
//        }
//    }
}
