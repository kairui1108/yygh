package com.atguigu.sms.mail.service.impl;

import com.atguigu.sms.mail.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/1/10 18:49
 */
@Service
public class MailServiceImpl implements MailService {

    @Resource
    private JavaMailSender javaMailSender;

    public static final String TEMPLATE = "【尚医通】您的邮箱登录验证码为%s，2分钟内有效。验证码切勿提供给他人，否则可能导致账号被盗用。";

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public boolean sendMail(String to, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setSubject("尚医通|验证码");
        message.setText(String.format(TEMPLATE, text));
        message.setTo(to);
        javaMailSender.send(message);
        return true;
    }

}
