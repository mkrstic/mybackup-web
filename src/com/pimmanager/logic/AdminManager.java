/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pimmanager.logic;

import com.pimmanager.beans.UserService;
import com.pimmanager.beans.WebAdmin;
import com.pimmanager.beans.exceptions.EmailException;
import com.pimmanager.configuration.AppConfig;
import com.pimmanager.util.AppUtils;
import com.pimmanager.util.MessageManager;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;


// TODO: Auto-generated Javadoc
/**
 * The Class AdminManager.
 *
 * @author mladen
 */
@ManagedBean(name = "adminManager")
@SessionScoped
public class AdminManager implements UserService, Serializable {

    /** The admin. */
    private WebAdmin admin;

    /**
     * Instantiates a new admin manager.
     */
    public AdminManager() {
        admin = new WebAdmin();
    }

    /* (non-Javadoc)
     * @see com.pimmanager.beans.UserService#login()
     */
    public Integer login() {
        String adminUsername = AppConfig.getAdminUsername();
        String adminPassword = AppConfig.getAdminPassword();
        if (admin.getUsername().compareTo(adminUsername) == 0 && admin.getPassword().compareTo(adminPassword) == 0) {
            admin.setLoggedIn(true);
            return MessageManager.MESSAGE_SUCCESS;
        } else {
            return MessageManager.MESSAGE_FAILURE;
        }
    }

    /**
     * Login JSF action.
     *
     * @return the string
     */
    public String loginAction() {
        int loginResult = login();
        if (loginResult == MessageManager.MESSAGE_SUCCESS) {
            return "pretty:adminHome";
        }
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect username or password", "Incorrect username or password");
        FacesContext.getCurrentInstance().addMessage(null, message);
        return "";
    }

    /* (non-Javadoc)
     * @see com.pimmanager.beans.UserService#logout()
     */
    public Integer logout() {
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.invalidate();
        } catch (IllegalStateException ise) {
            ise.printStackTrace();
            return MessageManager.MESSAGE_ERROR;
        } catch (Exception ex) {
            ex.printStackTrace();
            return MessageManager.MESSAGE_ERROR;
        }
        getAdmin().setLoggedIn(false);
        setAdmin(null);
        return MessageManager.MESSAGE_SUCCESS;

    }

    /**
     * Logout JSF action.
     *
     * @return the string
     */
    public String logoutAction() {
        logout();
        return "pretty:adminLogin";
    }

    /**
     * Recover password action.
     */
    public void recoverPasswordAction() {
        FacesMessage facesMessage = null;
        int recoverResult = recoverPassword();
        if (recoverResult == MessageManager.MESSAGE_SUCCESS) {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Password sent", "Your password is sent on: " + admin.getEmail());
        } else if (recoverResult == MessageManager.MESSAGE_FAILURE) {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid data", "Incorrect e-mail");
        }
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }

    /**
     * Gets the admin.
     *
     * @return the admin
     */
    public WebAdmin getAdmin() {
        return admin;
    }

    /**
     * Sets the admin.
     *
     * @param admin the admin to set
     */
    public void setAdmin(WebAdmin admin) {
        this.admin = admin;
    }

    /* (non-Javadoc)
     * @see com.pimmanager.beans.UserService#register()
     */
    public Integer register() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see com.pimmanager.beans.UserService#recoverPassword()
     */
    public Integer recoverPassword() {
        String adminEmail = AppConfig.getAdminEmail();
        if (!adminEmail.equals(admin.getEmail())) {
            return MessageManager.MESSAGE_FAILURE;
        }
        Thread emailer = new Thread() {

            String adminUsername = AppConfig.getAdminUsername();
            String adminPassword = AppConfig.getAdminPassword();
            private String subject = "MyBackup Admin - password recovery";
            private String message =
                    "Dear administrator,\nYour username is: [$username]"
                    + "\nYour password is: [$password]"
                    + "\nYou can now login.\nMyBackup team".replace("[$username]", adminUsername).replace("[$password]", adminPassword);

            public void run() {
                try {
                    AppUtils.sendEmail("text/plain",
                            AppConfig.getAdminAutomaticMail(),
                            AppConfig.getAdminName(),
                            admin.getEmail(),
                            AppConfig.getAdminName(),
                            subject, message);
                } catch (EmailException ee) {
                }
            }
        };
        emailer.start();
        return MessageManager.MESSAGE_SUCCESS;
    }
}
