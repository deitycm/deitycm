package cm.deity.base.mail;

import cm.deity.base.utils.entrypt.AESUtil;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;

public class SpringMail{
    private static String host;
    private static String userName;
    private static String password;
    private final static transient Logger LOGGER = LogManager.getLogger(SpringMail.class);
    static {
        Properties props = new Properties();
        try {
            WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
            ServletContext servletContext = webApplicationContext.getServletContext();
            props.load(servletContext.getResourceAsStream("mail.properties"));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        host = props.getProperty("host");
        userName = props.getProperty("userName");
        password = AESUtil.decrypt(props.getProperty("password"));
    }

    public static void sendSimpleMail(SimpleMailMessage mail) throws GeneralSecurityException {
        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
        senderImpl.setHost(host);
        senderImpl.setUsername(userName);
        senderImpl.setPassword(password);
        Properties prop = new Properties();
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);  
        prop.put("mail.smtp.ssl.enable", "true");  
        prop.put("mail.smtp.ssl.socketFactory", sf);  
        prop.put("mail.smtp.auth", "true");  
        prop.put("mail.smtp.timeout", "20000");  
        senderImpl.setJavaMailProperties(prop);  
        mail.setFrom(userName);
        mail.setTo(userName);
        mail.setSubject("测试标题");  
        mail.setText("测试");  
        senderImpl.send(mail);  
        System.out.println("SIMPLEMAIL SENDED");  
    }


    public static void sendMimeMail() throws GeneralSecurityException, MessagingException {
        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();  
        senderImpl.setHost(host);
        senderImpl.setUsername(userName);
        senderImpl.setPassword(password);
        Properties prop = new Properties();  
        MailSSLSocketFactory sf = new MailSSLSocketFactory();  
        sf.setTrustAllHosts(true);  
        prop.put("mail.smtp.ssl.enable", "true");  
        prop.put("mail.smtp.ssl.socketFactory", sf);  
        prop.put("mail.smtp.auth", "true");  
        prop.put("mail.smtp.timeout", "20000");   
        senderImpl.setJavaMailProperties(prop);  
        MimeMessage mail = senderImpl.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail,true);
        helper.setFrom(userName);
        helper.setTo(userName);
        helper.setSubject("测试标题MIME");  
        helper.setText("测试MIME");  
        FileSystemResource mailImage = new FileSystemResource("src/main/webapp/files/images/mail.png");
        helper.addAttachment("mail.png", mailImage);  
        senderImpl.send(mail);  
        System.out.println("MIMEMAIL SENDED");  
    }

    public static void sendHTMLMail() throws GeneralSecurityException, MessagingException{  
        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();  
        senderImpl.setHost(host);
        senderImpl.setUsername(userName);
        senderImpl.setPassword(password);
        senderImpl.setDefaultEncoding("UTF-8");  
        Properties prop = new Properties();  
        MailSSLSocketFactory sf = new MailSSLSocketFactory();  
        sf.setTrustAllHosts(true);  
        prop.put("mail.smtp.ssl.enable", "true");  
        prop.put("mail.smtp.ssl.socketFactory", sf);  
        prop.put("mail.smtp.auth", "true");  
        prop.put("mail.smtp.timeout", "20000");   
        senderImpl.setJavaMailProperties(prop);  
        MimeMessage mail = senderImpl.createMimeMessage();  
        MimeMessageHelper helper = new MimeMessageHelper(mail,true);  
        helper.setFrom(userName);
        helper.setTo(password);
        helper.setSubject("测试标题HTML");  
        StringBuilder html = new StringBuilder();  
        html.append("<html>")  
            .append("<body>")  
            .append("<h2>Goser,你好</h2>")  
            .append("<p>这是一个测试。</p>")  
            .append("<img src='cid:mailImage'/>")  
            .append("<p>THANKS</p>")  
            .append("</body>")  
            .append("</html>");  
        helper.setText(html.toString(),true);  
        FileSystemResource mailImage = new FileSystemResource("src/main/webapp/files/images/mail.png");
        helper.addInline("mailImage", mailImage);  
        senderImpl.send(mail);  
        System.out.println("HTMLMAIL SENDED");  
    }  
    public static void main(String[] args) throws GeneralSecurityException, MessagingException{  
        sendSimpleMail(new SimpleMailMessage());
    }