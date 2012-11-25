/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pimmanager.util;

import com.pimmanager.configuration.AppConfig;
import com.pimmanager.beans.exceptions.EmailException;
import com.pimmanager.beans.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Properties;
import java.util.UUID;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.validator.EmailValidator;

// TODO: Auto-generated Javadoc
/**
 * The Class AppUtils.
 *
 * @author mladen
 */
public class AppUtils {

    /** The CONTEN t_ typ e_ html. */
    public static String CONTENT_TYPE_HTML = "text/html";
    
    /** The CONTEN t_ typ e_ text. */
    public static String CONTENT_TYPE_TEXT = "text/plain";

   
    /**
     * Instantiates a new app utils.
     */
    private AppUtils() {
    }

    /**
     * Normalize phone number.
     *
     * @param userNumber the user number
     * @return the string
     */
    public static String normalizePhoneNumber(String userNumber)
    {
        int length = userNumber.trim().length();
        if(length <= 9) 
        {
            return "254".concat(userNumber.trim());
        }
        int startIndex = length - 9;
        return "254".concat(userNumber.trim().substring(startIndex));
    }

    /**
     * Read file.
     *
     * @param path the path
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static String readFile(String path) throws IOException {
        FileInputStream stream = new FileInputStream(new File(path));
        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            return Charset.defaultCharset().decode(bb).toString();
        } finally {
            stream.close();
        }
    }

    /**
     * Find component.
     *
     * @param parent the parent
     * @param id the id
     * @return the uI component
     */
    public static UIComponent findComponent(UIComponent parent, String id) {
        if(id.equals(parent.getId())) {
                return parent;
        }
        Iterator<UIComponent> kids = parent.getFacetsAndChildren();
        while(kids.hasNext()) {
                UIComponent kid = kids.next();
                UIComponent found = findComponent(kid, id);
                if(found != null) {
                        return found;
                }
        }
        return null;
    }
    
    /**
     * Adds the message.
     *
     * @param componentName the component name
     * @param messageBody the message body
     * @param severity the severity
     */
    public static void addMessage(String componentName, String messageBody, FacesMessage.Severity severity) {
        FacesMessage message = new FacesMessage(messageBody);
        message.setSeverity(severity);
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent component = findComponent(context.getViewRoot(), componentName);
        context.addMessage(component.getClientId(context), message);
    }
    
    /**
     * Adds the message.
     *
     * @param componentName the component name
     * @param message the message
     */
    public static void addMessage(String componentName, FacesMessage message) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent component = findComponent(context.getViewRoot(), componentName);
        context.addMessage(component.getClientId(context), message);
    }
    
    // returns immutable universally unique identifier (UUID). 128-bit value.
    /**
     * Gets the unique key.
     *
     * @return the unique key
     */
    public static String getUniqueKey() {
        UUID randomKey = UUID.randomUUID();
        return randomKey.toString();

    }
    
    /**
     * Send email confirmation.
     *
     * @param user the user
     * @param userKey the user key
     * @param baseURL the base url
     */
    public static void sendEmailConfirmation(User user, String userKey, final String baseURL) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", AppConfig.getSmtpHost());
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", String.valueOf(AppConfig.isSmtpAuth()));
            props.put("mail.smtp.port", AppConfig.getSmtpPort());
            props.put("mail.smtp.connectiontimeout", "60000");
            props.put("mail.smtp.timeout", "60000");
            

            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(AppConfig.getSmtpUsername(), AppConfig.getSmtpPassword());
                }
            };
            Session session = Session.getDefaultInstance(props, auth);
            session.setDebug(AppConfig.isSmtpDebug());
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(AppConfig.getAdminAutomaticMail(), "MyBackup team"));
            msg.addRecipient(Message.RecipientType.TO,
                            new InternetAddress(user.getEmail(), "Mr./Ms. " + user.getLastName()));
            String activationURL = baseURL + "?id=" + user.getId() + "&key=" + userKey;
            msg.setSubject("Confirm registration");
            String editableMsgContent;
            try {
                String path = (getWebInfPath() + "emailPages" + java.io.File.separator + "emailActivation.html").replace("%20", " ");
                editableMsgContent = AppUtils.readFile(path);
        } catch (IOException ioe) {
            editableMsgContent =
               "Dear <b>" + user.getFirstName() + "</b><p></p><p>Your registration was successful.</p>" +
                    "<p>To complete registration, please activate your account by one click " +
                    "<a href=\"" + activationURL + "\">here</a> or simply copy URL below in your favorite browser:</p> " +
                    activationURL + "<p></p><p>Regards</p>";
        }
        final String msgContent =
                editableMsgContent.replace("[$firstName]", user.getFirstName())
                .replace("[$lastName]", user.getLastName())
                .replace("[$email]", user.getEmail())
                .replace("[$password]", user.getPassword())
                .replace("[$activationURL]", activationURL);
            msg.setContent(msgContent, "text/html; charset=utf-8");
            Transport.send(msg);
        } catch (AddressException ae) {
        } catch (MessagingException me) {
        } catch (UnsupportedEncodingException uee) {
        }
    }

    /**
     * Gets the request url.
     *
     * @return the request url
     */
    public static String getRequestURL() {
        Object request = FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if(request instanceof HttpServletRequest) {
            return ((HttpServletRequest) request).getRequestURL().toString();
        } else {
            return "";
        }
    }

    /**
     * Gets the web inf path.
     *
     * @return the web inf path
     */
    public static String getWebInfPath(){
        final String WEBINF = "WEB-INF";
        String filePath = "";
        URL url = AppUtils.class.getResource("AppUtils.class");
        String className = url.getFile();
        filePath = className.substring(0, className.indexOf(WEBINF) + WEBINF.length()) + "/";
        return filePath.replace("%20", " ");
  }

    /**
     * Send email.
     *
     * @param contentType the content type
     * @param fromEmail the from email
     * @param fromName the from name
     * @param toEmail the to email
     * @param toName the to name
     * @param subject the subject
     * @param message the message
     * @throws EmailException the email exception
     */
    public static void sendEmail(String contentType, String fromEmail, String fromName, String toEmail, String toName, String subject, String message)
        throws EmailException {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", AppConfig.getSmtpHost());
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", String.valueOf(AppConfig.isSmtpAuth()));
            props.put("mail.smtp.port", AppConfig.getSmtpPort());
            props.put("mail.smtp.connectiontimeout", "60000");
            props.put("mail.smtp.timeout", "60000");

            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(AppConfig.getSmtpUsername(), AppConfig.getSmtpPassword());
                }
            };
            Session session = Session.getDefaultInstance(props, auth);
            session.setDebug(AppConfig.isSmtpDebug());
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail, fromName));
            msg.addRecipient(Message.RecipientType.TO,
                            new InternetAddress(toEmail, toName));
            msg.setSubject(subject);
            msg.setContent(message, "text/html");
            Transport.send(msg);

        } catch (AddressException ae) {
            throw new EmailException(ae.getLocalizedMessage());
        } catch (MessagingException me) {
            throw new EmailException(me.getLocalizedMessage());
        } catch (UnsupportedEncodingException uee) {
            throw new EmailException(uee.getLocalizedMessage());
        }
    }
    
    /**
     * Checks if is email valid.
     *
     * @param entered the entered
     * @return true, if is email valid
     */
    public static boolean isEmailValid(String entered) {
        if (entered != null && entered.length() >= 5 && entered.length() <= 50 && EmailValidator.getInstance().isValid(entered)) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Checks if is phone valid.
     *
     * @param phone the phone
     * @return true, if is phone valid
     */
    public static boolean isPhoneValid(String phone) {
        if (phone == null || phone.trim().equals(""))
            return false;
        StringBuilder str = new StringBuilder(phone);
        char c;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (!(Character.isDigit(c) || Character.isSpaceChar(c))) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks if is password valid.
     *
     * @param entered the entered
     * @return true, if is password valid
     */
    public static boolean isPasswordValid(String entered) {
        if (entered != null && entered.length() >= 5 && entered.length() <= 20 && entered.indexOf(" ") == -1)
             return true;
         else
             return false;
    }



}
