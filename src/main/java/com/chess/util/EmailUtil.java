package com.chess.util;

import java.util.Properties;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtil {
    private static final String HOST = "smtp.gmail.com";
    private static final int PORT = 587;
    private static final String USERNAME = "your.chess.app@gmail.com"; // Replace with your email
    private static final String PASSWORD = "your-app-specific-password"; // Use app-specific password
    
    private static Properties getMailProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);
        return props;
    }
    
    private static Session getMailSession() {
        return Session.getInstance(getMailProperties(), new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
    }
    
    public static void sendPasswordResetEmail(String toEmail, String resetToken) throws MessagingException {
        Session session = getMailSession();
        Message message = new MimeMessage(session);
        
        message.setFrom(new InternetAddress(USERNAME));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Chess Game - Password Reset");
        
        String resetLink = "http://localhost:8080/GameChess/user/reset-password/" + resetToken;
        String htmlContent = String.format(
            "<html><body>" +
            "<h2>Password Reset Request</h2>" +
            "<p>You have requested to reset your password for your Chess Game account.</p>" +
            "<p>Click the following link to reset your password:</p>" +
            "<p><a href='%s'>Reset Password</a></p>" +
            "<p>If you did not request this, please ignore this email.</p>" +
            "<p>This link will expire in 24 hours.</p>" +
            "</body></html>",
            resetLink
        );
        
        message.setContent(htmlContent, "text/html; charset=utf-8");
        Transport.send(message);
    }
    
    public static void sendTournamentInvitation(String toEmail, String tournamentName, int tournamentId) throws MessagingException {
        Session session = getMailSession();
        Message message = new MimeMessage(session);
        
        message.setFrom(new InternetAddress(USERNAME));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Chess Game - Tournament Invitation");
        
        String tournamentLink = "http://localhost:8080/GameChess/tournament/" + tournamentId;
        String htmlContent = String.format(
            "<html><body>" +
            "<h2>Tournament Invitation</h2>" +
            "<p>You have been invited to participate in the tournament: %s</p>" +
            "<p>Click the following link to view the tournament details:</p>" +
            "<p><a href='%s'>View Tournament</a></p>" +
            "</body></html>",
            tournamentName,
            tournamentLink
        );
        
        message.setContent(htmlContent, "text/html; charset=utf-8");
        Transport.send(message);
    }
    
    public static void sendGameNotification(String toEmail, String opponent, int gameId) throws MessagingException {
        Session session = getMailSession();
        Message message = new MimeMessage(session);
        
        message.setFrom(new InternetAddress(USERNAME));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Chess Game - New Game Started");
        
        String gameLink = "http://localhost:8080/GameChess/game/" + gameId;
        String htmlContent = String.format(
            "<html><body>" +
            "<h2>New Game Started</h2>" +
            "<p>A new game has been started between you and %s.</p>" +
            "<p>Click the following link to play:</p>" +
            "<p><a href='%s'>Play Game</a></p>" +
            "</body></html>",
            opponent,
            gameLink
        );
        
        message.setContent(htmlContent, "text/html; charset=utf-8");
        Transport.send(message);
    }
}
