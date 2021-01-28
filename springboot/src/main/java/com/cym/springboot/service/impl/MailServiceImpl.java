package com.cym.springboot.service.impl;


import com.alibaba.druid.util.StringUtils;
import com.cym.springboot.entity.MailVo;
import com.cym.springboot.service.IMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * @Description: 邮件发送实现类
 * @Date: 2021/1/27 17:25
 * @Author: cym
 * @Version: 1.0
 */
@Service
public class MailServiceImpl implements IMailService {

    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
    @Autowired
    private JavaMailSenderImpl mailSender;

    @Override
    public MailVo sendMail(MailVo mailVo) throws Exception {
        checkMail(mailVo);
        sendMimeMail(mailVo);
        return mailVo;
    }


    /**
     * 检测邮件,首先校验邮件收信人、邮件主题和邮件内容这些必填项，若为空则拒绝发送。
     */
    private void checkMail(MailVo mailVo) {
        if (StringUtils.isEmpty(mailVo.getTo())) {
            throw new RuntimeException("邮件收信人不能为空");
        }
        if (StringUtils.isEmpty(mailVo.getSubject())) {
            throw new RuntimeException("邮件主题不能为空");
        }
        if (StringUtils.isEmpty(mailVo.getText())) {
            throw new RuntimeException("邮件内容不能为空");
        }
    }

    /**
     * 发送邮件,其次通过MimeMessageHelper来解析MailVo并构建MimeMessage传输邮件。
     */
    private void sendMimeMail(MailVo mailVo) {
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
            mailVo.setFrom(getMailSendFrom());
            messageHelper.setFrom(mailVo.getFrom());
            messageHelper.setTo(mailVo.getTo().split(","));
            messageHelper.setSubject(mailVo.getSubject());
            messageHelper.setText(mailVo.getText());
            if (!StringUtils.isEmpty(mailVo.getCc())) {
                messageHelper.setCc(mailVo.getCc().split(","));
            }
            if (!StringUtils.isEmpty(mailVo.getBcc())) {
                messageHelper.setCc(mailVo.getBcc().split(","));
            }
            if (mailVo.getMultipartFiles() != null) {
                for (MultipartFile multipartFile : mailVo.getMultipartFiles()) {
                    messageHelper.addAttachment(multipartFile.getOriginalFilename(), multipartFile);
                }
            }
            if (ObjectUtils.isEmpty(mailVo.getSentDate())) {
                mailVo.setSentDate(new Date());
                messageHelper.setSentDate(mailVo.getSentDate());
            }
            mailSender.send(messageHelper.getMimeMessage());
            logger.info("发送邮件成功：{}->{}", mailVo.getFrom(), mailVo.getTo());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 保存邮件,最后将邮件保存到数据库，便于统计和追查邮件问题。
     */
    private MailVo saveMail(MailVo mailVo) {
        return mailVo;
    }

    @Override
    public String getMailSendFrom() {
        return mailSender.getJavaMailProperties().getProperty("from");
    }
}
