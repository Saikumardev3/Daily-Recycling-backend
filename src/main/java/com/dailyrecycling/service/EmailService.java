package com.dailyrecycling.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@dailyrecycling.com}")
    private String fromEmail;

    // Back Office Email Mappings - Update these based on your requirements
    private static final Map<String, String> BACK_OFFICE_EMAILS = new HashMap<>();

    static {
        BACK_OFFICE_EMAILS.put("MIO", "backoffice.mio@example.com");
        BACK_OFFICE_EMAILS.put("miles", "backoffice.miles@example.com");
        BACK_OFFICE_EMAILS.put("Ww", "backoffice.ww@example.com");
        BACK_OFFICE_EMAILS.put("Santander", "backoffice.santander@example.com");
        BACK_OFFICE_EMAILS.put("FS", "backoffice.fs@example.com");
        BACK_OFFICE_EMAILS.put("F6", "backoffice.f6@example.com");
    }

    /**
     * Send email when BCU number is missing
     */
    public void sendBcuNumberMissingEmail(String policyNumber, String appCode) {
        String toEmail = getBackOfficeEmail(appCode);
        if (toEmail == null) {
            toEmail = "backoffice.default@example.com"; // Default email
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("BCU Number Missing - Action Required");
        message.setText(String.format(
            "Dear Back Office Team,\n\n" +
            "The following policy record requires manual intervention:\n\n" +
            "Policy Number: %s\n" +
            "App Code: %s\n" +
            "Issue: BCU Number is missing in the database\n\n" +
            "Please update the BCU number in the system.\n\n" +
            "Thank you,\n" +
            "Daily Recycling System",
            policyNumber, appCode
        ));

        mailSender.send(message);
    }

    /**
     * Send email when duration exceeds 9999 days
     */
    public void sendDurationExceedsEmail(String policyNumber, String appCode, int duration) {
        String toEmail = getBackOfficeEmail(appCode);
        if (toEmail == null) {
            toEmail = "backoffice.default@example.com";
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Duration Exceeds 9999 Days - Action Required");
        message.setText(String.format(
            "Dear Back Office Team,\n\n" +
            "The following policy record has been rejected:\n\n" +
            "Policy Number: %s\n" +
            "App Code: %s\n" +
            "Duration: %d days\n" +
            "Issue: Policy duration exceeds 9999 days\n\n" +
            "Please review and correct the policy dates.\n\n" +
            "Thank you,\n" +
            "Daily Recycling System",
            policyNumber, appCode, duration
        ));

        mailSender.send(message);
    }

    /**
     * Get back office email based on application code
     */
    private String getBackOfficeEmail(String appCode) {
        // Match app code to back office email
        for (Map.Entry<String, String> entry : BACK_OFFICE_EMAILS.entrySet()) {
            if (appCode.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
