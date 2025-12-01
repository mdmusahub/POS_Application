package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.Order;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 * Service responsible for sending emails and generating HTML templates,
 * specifically for sending order confirmation mails using Thymeleaf.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    /**
     * Sends an HTML email to the specified recipient.
     *
     * @param to          receiver email address
     * @param subject     email subject line
     * @param htmlContent HTML content to be sent
     * @throws RuntimeException if email sending fails
     */
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = HTML email

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    /**
     * Builds the HTML content for an order confirmation email using Thymeleaf.
     *
     * @param order the order whose details should appear in the email
     * @return the processed HTML string generated from the Thymeleaf template
     */
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

        // Pass item list to render in HTML template
        ctx.setVariable("items", order.getOrder_items());

        return templateEngine.process("email/order-confirmation", ctx);
    }

}
