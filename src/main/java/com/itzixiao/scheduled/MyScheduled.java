package com.itzixiao.scheduled;

import com.itzixiao.base.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
public class MyScheduled {

    @Autowired
    private SendMessage sendMessage;

//    @Scheduled(cron = "0/5 * * * * ?")//每隔5s
//    @Scheduled(cron = "0 0/10 * * * ?")//每隔10分钟
    @Scheduled(cron = "0 14 13 * * ?")//每天13点14分
    public void dsrw() {
        System.out.println("开始执行");
        String message = sendMessage.getOneS();

        System.out.println(message);
        try {
            sendMessage.sendTemplateMail("致亲爱的雪公主！❤", message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}