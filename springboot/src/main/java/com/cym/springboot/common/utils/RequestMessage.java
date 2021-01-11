package com.cym.springboot.common.utils;

/**
 * @Description: 返回接口API响应状态码
 * @Date: 2021/1/8 10:35
 * @Author: cym
 * @Version: 1.0
 */
public class RequestMessage<T> {

    /**
     * 服务器成功返回用户请求数据的状态吗
     */
    public static String SUCCESS_GETS_CODE = "200";

    /**
     * 服务器成功返回用户请求数据的提示信息
     */
    public static String SUCCESS_GETS_OK = "OK";

    /**
     * 新建或修改成功返回的状态吗
     */
    public static String SUCCESS_POSTS_CODE = "201";

    /**
     * 新建或修改成功返回的提示信息
     */
    public static String SUCCESS_POSTS_OK = "OK";

    /**
     * 删除数据成功返回的状态吗
     */
    public static String SUCCESS_DELETES_CODE = "204";

    /**
     * 删除数据成功返回的提示信息
     */
    public static String SUCCESS_DELETES_OK = "OK";

    /**
     * 服务器发生错误的状态吗
     */
    public static String ERROR_CODE = "500";

    /**
     * 服务器发生错误的提示信息
     */
    public static String ERROR_msg = "error";

    /**
     * 状态吗
     */
    private String code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 服务器异常信息
     */
    private String error;

    /**
     * 成功返回的数据
     */
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
