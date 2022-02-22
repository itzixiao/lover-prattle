package com.itzixiao.base;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class SendMessage {

    @Autowired
    private JavaMailSenderImpl mailSender;

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
        //要带附件第二个参数设为true
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
     * 远程获取要发送的信息
     */
    public static String getOneS() {
        try {
            //创建客户端对象
            HttpClient client = HttpClients.createDefault();
            /*创建地址 https://du.shadiao.app/api.php*/
            HttpGet get = new HttpGet("https://chp.shadiao.app/api.php");
            //发起请求，接收响应对象
            HttpResponse response = client.execute(get);
            //获取响应体，响应数据是一种基于HTTP协议标准字符串的对象
            //响应体和响应头，都是封装HTTP协议数据。直接使用可能出现乱码或解析错误
            HttpEntity entity = response.getEntity();
            //通过HTTP实体工具类，转换响应体数据
            String responseString = EntityUtils.toString(entity, "utf-8");

            return responseString;
        } catch (IOException e) {
            throw new RuntimeException("网站获取句子失败");
        }
    }
}