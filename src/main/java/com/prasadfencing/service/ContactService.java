package com.prasadfencing.service;

import com.prasadfencing.dto.ContactRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final JavaMailSender mailSender;

    public void sendContactEmails(ContactRequest request) {
        sendThankYouEmail(request);
        sendOwerNotification(request);
    }

    private void sendThankYouEmail(ContactRequest request)
    {
        SimpleMailMessage message= new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject(
                "Thank You For Contacting Us"
        );

        message.setText("Hello "+ request.getName()+",\n\n"+
                "Thank you for contacting us. \n"+
                "We received your message and will respond soon.\n\n"+
                "Regards,\n"+
                "Prasad Fencing"
        );

        mailSender.send(message);
    }

    private void sendOwerNotification(ContactRequest request){

        SimpleMailMessage message=new SimpleMailMessage();

        message.setTo("anonymoussamachar@gmail.com");

        message.setSubject("New Contact From Submission");

        message.setText(
                "Customer Details:\n\n" +
                        "Name: " + request.getName() + "\n" +
                        "Email: " + request.getEmail() + "\n" +
                        "Phone_no: "+request.getPhone_no()+"\n" +
                        "Subject: " + request.getSubject() + "\n" +
                        "Message: " + request.getMessage()
        );
        mailSender.send(message);
    }
}
