package com.cym.springboot.common.utils;

import java.util.List;

/**
 * @Description: 接口响应数据封装累
 * @Date: 2021/1/8 11:20
 * @Author: cym
 * @Version: 1.0
 */
public class ResponseUtil {

    /**
     * 操作成功，返回没有分页的列表
     *
     * @param records
     * @param <T>
     * @return
     */
    public static <T> RequestMessage<T> successObjectResponse(T records) {
        RequestMessage<T> requestMessage = new RequestMessage();
        requestMessage.setCode(RequestMessage.SUCCESS_GETS_CODE);
        requestMessage.setMessage(RequestMessage.SUCCESS_GETS_OK);
        requestMessage.setData(records);
        return requestMessage;
    }

    /**
     * 操作成功，返回没有分页的列表
     *
     * @param records
     * @return
     */
    public static <T> RequestMessage<T> successListResponse(List records) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setCode(RequestMessage.SUCCESS_GETS_CODE);
        requestMessage.setMessage(RequestMessage.SUCCESS_GETS_OK);
        requestMessage.setData(records);
        return requestMessage;
    }

    /**
     * 操作成功，只返回结果，没有记录
     *
     * @param msg
     * @return
     */
    public static RequestMessage successResponse(String msg) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setCode(RequestMessage.SUCCESS_GETS_CODE);
        if (null == msg || "".equals(msg)) {
            requestMessage.setMessage(RequestMessage.SUCCESS_GETS_OK);
        } else {
            requestMessage.setMessage(msg);
        }
        return requestMessage;
    }

    /**
     * 操作成功，只返回结果，没有记录(查询)
     *
     * @param msg
     * @return
     */
    public static RequestMessage successResponseGets(String msg) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setCode(RequestMessage.SUCCESS_GETS_CODE);
        if (null == msg || "".equals(msg)) {
            requestMessage.setMessage(RequestMessage.SUCCESS_GETS_OK);
        } else {
            requestMessage.setMessage(msg);
        }
        return requestMessage;
    }

    /**
     * 操作成功，只返回结果，没有记录(新增、修改)
     *
     * @param msg
     * @return
     */
    public static RequestMessage successResponsePosts(String msg) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setCode(RequestMessage.SUCCESS_POSTS_CODE);
        if (null == msg || "".equals(msg)) {
            requestMessage.setMessage(RequestMessage.SUCCESS_POSTS_OK);
        } else {
            requestMessage.setMessage(msg);
        }
        return requestMessage;
    }

    /**
     * 操作成功，只返回结果，没有记录(删除)
     *
     * @param msg
     * @return
     */
    public static RequestMessage successResponseDeletes(String msg) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setCode(RequestMessage.SUCCESS_DELETES_CODE);
        if (null == msg || "".equals(msg)) {
            requestMessage.setMessage(RequestMessage.SUCCESS_DELETES_OK);
        } else {
            requestMessage.setMessage(msg);
        }
        return requestMessage;
    }

    /**
     * 操作失败，返回错误描述
     *
     * @param msg
     * @param error
     * @return
     */
    public static RequestMessage faliedResponse(String msg, String error) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setCode(RequestMessage.ERROR_CODE);
        if (null == msg || "".equals(msg)) {
            requestMessage.setMessage(RequestMessage.ERROR_msg);
        } else {
            requestMessage.setMessage(msg);
        }
        requestMessage.setError(error);
        return requestMessage;
    }
}
