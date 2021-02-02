package com.cym.springboot.service;

/**
 * @Description: 短信发送接口
 * @Date: 2021/2/1 11:14
 * @Author: cym
 * @Version: 1.0
 */
public interface ISmsService {

    /**
     * 发送短信
     *
     * @param phones 多个逗号隔开
     * @throws Exception
     */
    void sendSms(String phones) throws Exception;

    /**
     * 短信验证
     *
     * @param phone 手机号
     * @param code  验证码
     * @return
     */
    boolean validSmsCode(String phone, String code);
}
