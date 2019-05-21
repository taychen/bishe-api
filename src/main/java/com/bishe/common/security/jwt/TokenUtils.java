//package com.bishe.common.security.jwt;
//
//import com.iot.zx.common.exception.TokenException;
//import com.iot.zx.common.properties.ResultEnums;
//import com.iot.zx.common.properties.RoleEnums;
//import com.iot.zx.user.entity.UserInfoEntity;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * token处理辅助类
// *
// * @author chentay
// * @date 2019/04/13
// */
//public class TokenUtils {
//
//    private TokenUtils() {
//
//    }
//
//    private static class HolderClass {
//        private final static TokenUtils INSTANCE = new TokenUtils();
//    }
//
//    public static TokenUtils getInstance() {
//        return HolderClass.INSTANCE;
//    }
//
//    /**
//     * 无权限操作
//     *
//     * @param request 请求
//     */
//    public UserInfoEntity isPermission(HttpServletRequest request){
//        UserInfoEntity userInfo = JwtUtils.getInstance().getUserInfo(request);
//        if (userInfo == null) {
//            throw new TokenException(ResultEnums.PASS_NO_AUTH);
//        }
//        return userInfo;
//    }
//
//    /**
//     * 无超管权限
//     *
//     * @param request 请求
//     */
//    public UserInfoEntity isSuperAdmin(HttpServletRequest request){
//        UserInfoEntity userInfo = JwtUtils.getInstance().getUserInfo(request);
//        if (userInfo == null) {
//            throw new TokenException(ResultEnums.PASS_NO_AUTH);
//        }
//        if (!userInfo.getRoleValue().equals(RoleEnums.SUPER_ADMIN.getValue())) {
//            throw new TokenException(ResultEnums.PASS_NO_AUTH);
//        }
//        return userInfo;
//    }
//}
