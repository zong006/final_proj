package vttp.final_backend.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendEmailService{
    
    @Autowired
    private JavaMailSender mailSender;    

    

    public String sendEmail(String recepient, String emailSubject, String emailBody){
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("doomscrollerr0@gmail.com");
            message.setTo(recepient);
            message.setSubject(emailSubject);
            message.setText(emailBody);

            mailSender.send(message);

            return "Success";
        } catch (MailException e) {
            
            return e.getMessage();
        }
    }
}

