package com.cym.springboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * @Description: 邮件发送模板类
 * @Date: 2021/1/27 17:20
 * @Author: cym
 * @Version: 1.0
 */
public class MailVo {
    private String id;
    /**
     * 发件人邮箱
     */
    private String from;
    /**
     * 收件人邮箱
     */
    private String to;
    /**
     * 主题
     */
    private String subject;
    /**
     * 内容
     */
    private String text;
    /**
     * 发送时间
     */
    private Date sentDate;
    /**
     * 抄送人
     */
    private String cc;
    /**
     * 密送人
     */
    private String bcc;
    /**
     * 附件
     */
    @JsonIgnore
    private MultipartFile[] multipartFiles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public MultipartFile[] getMultipartFiles() {
        return multipartFiles;
    }

    public void setMultipartFiles(MultipartFile[] multipartFiles) {
        this.multipartFiles = multipartFiles;
    }
}
