package com.mecaps.posDev.ServiceImplementation;//package com.mecaps.posDev.ServiceImplementation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailService {
//    @Autowired
//    private JavaMailSender mailSender;
//
//    public void sendEmail(String to, String subject, String text) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(text);
//        mailSender.send(message);
//        System.out.println("Email sent successfully to " + to);
//    }
//
//}


import com.mecaps.posDev.Entity.Order;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = HTML

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    public String buildOrderConfirmationHtml(Order order) {
        Context ctx = new Context();

        ctx.setVariable("orderId", order.getOrderId());
        ctx.setVariable("totalAmount", order.getTotal_amount());
        ctx.setVariable("paymentMode", order.getPayment_mode());
        ctx.setVariable("status", order.getOrder_status());
        ctx.setVariable("discount", order.getDiscount());
        ctx.setVariable("tax", order.getTax());
        ctx.setVariable("cashAmount", order.getCash_amount());
        ctx.setVariable("onlineAmount", order.getOnline_amount());

        // MOST IMPORTANT → Pass product list
        ctx.setVariable("items", order.getOrder_items());

        return templateEngine.process("email/order-confirmation", ctx);
    }

}
