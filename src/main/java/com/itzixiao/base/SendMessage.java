package com.itzixiao.base;

import com.alibaba.fastjson.JSON;
import com.itzixiao.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
public class SendMessage {

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Value("${spring.mail.api}")
    private String api;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${she.mail}")
    private String[] sheMail;

    @Resource
    TemplateEngine templateEngine;

    /**
     * 发送模板邮件
     *
     * @param subject
     * @param message
     * @throws MessagingException
     */
    public void sendTemplateMail(String subject, String message) throws MessagingException {
        Context context = new Context();
//      设置传入模板的页面的参数 参数名为:id 参数随便写一个就行 
        context.setVariable("content", message);
        Date date = new Date();
        String pattern = "yyyy年MM月dd日 13:14:20";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        context.setVariable("date", simpleDateFormat.format(date));
//      emailTemplate是你要发送的模板我这里用的是Thymeleaf
        String process = templateEngine.process("email", context);

        MimeMessage mineMessage = mailSender.createMimeMessage();
        // 要带附件第二个参数设为true
        MimeMessageHelper helper = new MimeMessageHelper(mineMessage, true);
        helper.setFrom(from);
        helper.setTo(sheMail);
        helper.setSubject(subject);
        helper.setText(process, true);

        mailSender.setProtocol("smtp");
        mailSender.setPassword(password);
        mailSender.setPort(port);
        mailSender.setHost(host);
        mailSender.send(mineMessage);

    }

    /**
     * 调用文件
     *
     * @return
     */
    public String getOneS() {
        String result = HttpUtils.sendGet(api, null);
        Map<?, ?> map = JSON.parseObject(result, Map.class);
        return (String) ((Map<?, ?>) map.get("data")).get("text");
    }
}