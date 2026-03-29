package lk.ijse.aad.backend.Util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lk.ijse.aad.backend.Entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EMailService {

    private final JavaMailSender mailSender;

    public void sendBookingActionEmail(String toEmail, String providerName, String bookingId, User userDto) throws MessagingException {

        // URLs for accept/reject buttons
        String acceptUrl = "http://127.0.0.1:5500/FrontEnd/index.html";

        // HTML message with customer details
        String htmlMsg = "<h3>Hello " + providerName + ",</h3>"
                + "<p>You have a new booking request.</p>"
                + "<h4>Customer Details:</h4>"
                + "<ul>"
                + "<li><strong>Name:</strong> " + userDto.getName() + "</li>"
                + "<li><strong>Email:</strong> " + userDto.getEmail() + "</li>"
                + "<li><strong>Phone:</strong> " + userDto.getPhone() + "</li>"
                + "<li><strong>Province:</strong> " + userDto.getProvince() + "</li>"
                + "<li><strong>City:</strong> " + userDto.getCity() + "</li>"
                + "<li><strong>Village:</strong> " + userDto.getVillage() + "</li>"
                + "</ul>"
                + "<p>Please choose an action:</p>"
                + "<a href='" + acceptUrl + "' style='padding:10px 20px;background-color:green;color:white;text-decoration:none;margin-right:10px;'>Take Action</a>"
                + "<p>Thank you!</p>";

        // Create and send the email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(toEmail);
        helper.setSubject("New Booking Request");
        helper.setText(htmlMsg, true); // true = HTML

        mailSender.send(message);
    }

    public void sendBookingStatusToCustomer(String toEmail, String providerName, String bookingStatus) throws MessagingException {

        String htmlMsg = "<h3>Hello,</h3>"
                + "<p>Your booking with <strong>" + providerName + "</strong> has been <strong>" + bookingStatus + "</strong>.</p>"
                + "<p>Thank you for using our service!</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(toEmail);
        helper.setSubject("Booking " + bookingStatus);
        helper.setText(htmlMsg, true);

        mailSender.send(message);
    }

}