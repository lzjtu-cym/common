package com.cym.springboot.service;

import com.cym.springboot.entity.MailVo;

/**
 * @Description: 邮件发送接口
 * @Date: 2021/1/27 17:22
 * @Author: cym
 * @Version: 1.0
 */

public interface IMailService {

    /**
     * 发送邮件
     *
     * @param mailVo
     * @return
     * @throws Exception
     */
    MailVo sendMail(MailVo mailVo) throws Exception;

    /**
     * getMailSendFrom
     * @return
     */
    String getMailSendFrom();
}
