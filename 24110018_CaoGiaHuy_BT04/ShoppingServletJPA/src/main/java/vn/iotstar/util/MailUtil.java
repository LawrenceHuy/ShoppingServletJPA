package vn.iotstar.util;

import java.io.InputStream;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * Gửi email qua SMTP (mặc định Gmail). Cấu hình lấy từ biến môi trường
 * MAIL_USERNAME / MAIL_PASSWORD (ưu tiên) hoặc file src/main/resources/mail.properties (KHÔNG commit file này).
 */
public final class MailUtil {
    private static final Properties CFG = load();

    private MailUtil() {}

    private static Properties load() {
        Properties p = new Properties();
        try (InputStream in = MailUtil.class.getResourceAsStream("/mail.properties")) {
            if (in != null) p.load(new java.io.InputStreamReader(in, java.nio.charset.StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String u = System.getenv("MAIL_USERNAME");
        String pw = System.getenv("MAIL_PASSWORD");
        if (u != null && !u.isBlank()) p.setProperty("mail.username", u);
        if (pw != null && !pw.isBlank()) p.setProperty("mail.password", pw);
        return p;
    }

    public static boolean isConfigured() {
        String u = CFG.getProperty("mail.username", "");
        String p = CFG.getProperty("mail.password", "");
        return !u.isBlank() && !p.isBlank() && !u.startsWith("your_");
    }

    public static void send(String to, String subject, String htmlBody) throws MessagingException {
        final String username = CFG.getProperty("mail.username");
        final String password = CFG.getProperty("mail.password");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", CFG.getProperty("mail.host", "smtp.gmail.com"));
        props.put("mail.smtp.port", CFG.getProperty("mail.port", "587"));
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        MimeMessage msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(username, CFG.getProperty("mail.from.name", "Shopping MVC"), "UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            msg.setFrom(new InternetAddress(username));
        }
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
        msg.setSubject(subject, "UTF-8");
        msg.setContent(htmlBody, "text/html; charset=UTF-8");
        Transport.send(msg);
    }
}
