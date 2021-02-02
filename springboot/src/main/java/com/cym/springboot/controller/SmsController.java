package com.cym.springboot.controller;

import com.cym.springboot.common.utils.ResponseUtil;
import com.cym.springboot.service.ISmsService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 邮件发送控制类
 * @Date: 2021/1/27 17:21
 * @Author: cym
 * @Version: 1.0
 */
@RestController
@Api(description = "邮件发送 - 控制层")
@RequestMapping("sms")
public class SmsController {
    private static final Logger logger = LoggerFactory.getLogger(SmsController.class);

    @Autowired
    private ISmsService smsService;

    @PostMapping("/sendSms")
    public Object sendMail(String phones) {
        try {
            smsService.sendSms(phones);
            return ResponseUtil.successResponse("发送成功");
        } catch (Exception e) {
            logger.error("发送短信失败", e);
            return ResponseUtil.faliedResponse("发送短信失败！", e.getMessage());
        }
    }
}
