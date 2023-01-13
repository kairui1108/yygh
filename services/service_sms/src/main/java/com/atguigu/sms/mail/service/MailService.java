package com.atguigu.sms.mail.service;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/10 18:49
 */
public interface MailService {

    boolean sendMail(String to, String text);
}
