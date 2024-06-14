package com.qiu.apifinal.listener;




import com.qiu.apifinal.entity.dto.EmailMessage;
import com.qiu.apifinal.service.EmailService;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MailQueueListener {

    @Resource
    EmailService emailService;
    @RabbitListener(queues = "emailQueue")
    public void receiveMessage(EmailMessage emailMessage) {
        emailService.sendEmail(emailMessage.getEmail(), emailMessage.getSubject(), emailMessage.getText());
    }
}
