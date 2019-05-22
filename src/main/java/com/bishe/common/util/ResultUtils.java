package com.bishe.common.util;

import com.alibaba.fastjson.JSONObject;
import com.bishe.common.properties.ResultEnums;
import com.bishe.common.result.JsonResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 响应辅助类
 *
 * @author chentay
 * @date 2019/01/16
 */
public class ResultUtils {

    private ResultUtils() {

    }

    private static class HolderClass {
        private final static ResultUtils INSTANCE = new ResultUtils();
    }

    public static ResultUtils getInstance() {
        return HolderClass.INSTANCE;
    }

    /**
     * 默认返回
     *
     * @return {@link String}
     */
    public String toJSONSting() {
        return JSONObject.toJSONString(new JsonResult<>());
    }

    /**
     * 根据枚举进行返回
     *
     * @param resultEnums 枚举实例
     * @return {@link String} json字符串
     */
    public String toJSONString(ResultEnums resultEnums) {
        JsonResult result = new JsonResult();
        result.setCode(resultEnums.getCode());
        result.setMessage(resultEnums.getMessage());
        return JSONObject.toJSONString(result);
    }

    /**
     * 根据枚举进行返回
     *
     * @param resultEnums 枚举实例
     * @return {@link String} json字符串
     */
    public String toJSONString(ResultEnums resultEnums, Object data) {
        JsonResult<Object> result = new JsonResult<>();
        result.setCode(resultEnums.getCode());
        result.setMessage(resultEnums.getMessage());
        result.setData(data);
        return JSONObject.toJSONString(result);
    }

    /**
     * 无响应数据时返回
     *
     * @param resultEnums 枚举实例
     * @return {@link JSONObject} json
     */
    public JSONObject toJSON(ResultEnums resultEnums) {
        JSONObject result = new JSONObject();
        result.put("code", resultEnums.getCode());
        result.put("message", resultEnums.getMessage());
        return result;
    }

    /**
     * 无响应数据时返回
     *
     * @param code    响应码
     * @param message 提示信息
     * @return {@link String} json字符串
     */
    public String toJSON(int code, String message) {
        JsonResult result = new JsonResult();
        result.setCode(code);
        result.setMessage(message);
        return JSONObject.toJSONString(result);
    }

    /**
     * 无响应数据时返回
     *
     * @param code    响应码
     * @param message 提示信息
     * @param data    响应数据
     * @return {@link String} json字符串
     */
    public String toJSON(int code, String message, Object data) {
        JsonResult<Object> result = new JsonResult<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return JSONObject.toJSONString(result);
    }

    /**
     * Token响应
     *
     * @param token token
     * @return {@link String} json字符串
     */
    public String toJSONByToken(String token){
        JSONObject json = new JSONObject();
        json.put("code",ResultEnums.SUCCESS.getCode());
        json.put("access_token",token);
        return JSONObject.toJSONString(json);
    }

    /**
     * 将json输出到前端(参数非json格式)
     *
     * @param response 响应
     * @param json     json字符串
     */
    public void writeJavaScript(HttpServletResponse response, String json) {
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store, max-age=0, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        /* 设置浏览器跨域访问 */
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE,PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        try {
            PrintWriter out = response.getWriter();
            out.write(json);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
