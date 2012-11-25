/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pimmanager.logic;

import com.pimmanager.beans.exceptions.EmailException;
import com.pimmanager.configuration.AppConfig;
import com.pimmanager.util.AppUtils;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;


// TODO: Auto-generated Javadoc
/**
 * The Class ContactUsService.
 *
 * @author mladen
 */
@ManagedBean(name="contactUsBean")
@RequestScoped
public class ContactUsService implements Serializable {

    /** The email. */
    @NotEmpty @Email(message="Entered e-mail address is not valid")
    private String email;
    
    /** The name. */
    @Length(max=70) private String name;
    
    /** The message. */
    @Length(max=1000) private String message;

    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     *
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * Process.
     *
     * @param event the event
     */
    public void process(ActionEvent event) {
        Thread t = new Thread() {
            String subject = "MyBackup - automatic message forwarded from Contact us page";
            @Override
            public void run() {
                try {
                    AppUtils.sendEmail(
                            AppUtils.CONTENT_TYPE_TEXT, email, name,
                            AppConfig.getAdminEmail(),
                            AppConfig.getAdminName(),
                            subject, message);
                } catch (EmailException ex) {
                    // ignore...
                }
            }
        };
        t.start();
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Message sent", "Thank you. Your message is sent.");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }

    /**
     * Clear.
     */
    public void clear() {
        setEmail("");
        setName("");
        setMessage("");
    }

}
