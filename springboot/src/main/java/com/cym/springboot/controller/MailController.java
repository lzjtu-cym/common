package com.cym.springboot.controller;

import com.cym.springboot.common.utils.ResponseUtil;
import com.cym.springboot.entity.MailVo;
import com.cym.springboot.service.IMailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: 邮件发送控制类
 * @Date: 2021/1/27 17:21
 * @Author: cym
 * @Version: 1.0
 */
@RestController
@Api(description = "邮件发送 - 控制层")
@RequestMapping("mail")
public class MailController {
    private static final Logger logger = LoggerFactory.getLogger(MailController.class);

    @Autowired
    private IMailService mailService;

    @ApiOperation(value = "用户表 - 通过id查询", notes = "用户查询")
    @GetMapping("sendForm")
    public ModelAndView sendForm() {
        ModelAndView mv = new ModelAndView("sendMail");
        mv.addObject("from", mailService.getMailSendFrom());
        return mv;
    }

    @PostMapping("/sendMail")
    public Object sendMail(MailVo mailVo, MultipartFile[] files) {
        try {
            mailVo.setMultipartFiles(files);
            return ResponseUtil.successObjectResponse(mailService.sendMail(mailVo));
        } catch (Exception e) {
            logger.error("发送邮件失败", e);
            return ResponseUtil.faliedResponse("查询失败！", e.getMessage());
        }
    }
}
