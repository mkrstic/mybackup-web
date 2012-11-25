/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pimmanager.logic;

import java.io.Serializable;
import java.util.Date;
import java.util.zip.CRC32;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.primefaces.event.FlowEvent;

import com.pimmanager.beans.AdditionalUserData;
import com.pimmanager.beans.User;
import com.pimmanager.beans.UserService;
import com.pimmanager.beans.exceptions.EmailException;
import com.pimmanager.configuration.AppConfig;
import com.pimmanager.util.AppUtils;
import com.pimmanager.util.DAO;
import com.pimmanager.util.MessageManager;

// TODO: Auto-generated Javadoc
/**
 * The Class UserManager.
 *
 * @author mladen
 */
@ManagedBean(name = "userBean")
@SessionScoped
public class UserManager implements UserService, Serializable {

    /** The user. */
    private User user = new User();
    
    /** The add user data. */
    private AdditionalUserData addUserData;

    /**
     * Instantiates a new user manager.
     */
    public UserManager() {
    }

    /* (non-Javadoc)
     * @see com.pimmanager.beans.UserService#logout()
     */
    public Integer logout() {
        try {
            HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            httpSession.invalidate();
            if (user != null) {
                Session session = DAO.getSessionFactory().openSession();
                try {
                    user.setDateLastLogin(new Date());
                    session.beginTransaction();
                    session.update(user);
                    session.getTransaction().commit();
                    user.clear();
                } catch (HibernateException he) {
                    session.getTransaction().rollback();
                    he.printStackTrace();
                } finally {
                    if (session.isOpen()) {
                        session.close();
                    }
                }
            }
        } catch (IllegalStateException ise) {
            return MessageManager.MESSAGE_ERROR;
        } catch (Exception ex) {
            return MessageManager.MESSAGE_ERROR;
        }
        return MessageManager.MESSAGE_SUCCESS;
    }

    /**
     * Logout action.
     *
     * @return the string
     */
    public String logoutAction() {
        logout();
        return "pretty:home";
    }

    /**
     * Delete all contacts action.
     *
     * @param actionEvent the action event
     */
    public void deleteAllContactsAction(ActionEvent actionEvent) {
        FacesMessage message = null;
        Session session = DAO.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query q = session.createQuery("delete from Contact where user = :user");
            q.setParameter("user", user);
            q.executeUpdate();
            session.getTransaction().commit();
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Your contacts has been deleted.");
        } catch (HibernateException dbe) {
            dbe.printStackTrace();
            session.getTransaction().rollback();
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cannot delete your contacts. Please, try again.");
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /* (non-Javadoc)
     * @see com.pimmanager.beans.UserService#recoverPassword()
     */
    public Integer recoverPassword() {
        Session session = DAO.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("from User where phone = :phone").setString("phone", user.getPhone());
            user = (User) query.uniqueResult();
        } catch (HibernateException dbe) {
            dbe.printStackTrace();
            return MessageManager.MESSAGE_ERROR;
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        if (user == null || user.getEmail() == null || user.getEmail().trim().equals("")) {
            return MessageManager.MESSAGE_FAILURE;
        }
        final String msgSubject = "Password recovery";
        String msgContentPattern =
                "Dear Mr./Ms. <b>[$lastName]"
                + "</b><p></p><p>Your password is:</p><p>"
                + "[$password]</p><p>You can now login.</p>"
                + "<p></p><p>Regards,</p>" + "MyBackup team";
        final String msgContent =
                msgContentPattern.replace("[$firstName]", getUser().getFirstName()).replace("[$lastName]", getUser().getLastName()).replace("[$email]", getUser().getEmail()).replace("[$password]", getUser().getPassword());
        Runnable runnable = new Runnable() {

            public void run() {
                try {
                    AppUtils.sendEmail(AppUtils.CONTENT_TYPE_HTML, AppConfig.getAdminAutomaticMail(), "MyBackup team", getUser().getEmail(), getUser().getFirstName() + " " + getUser().getLastName(), msgSubject, msgContent);
                } catch (EmailException ex) {
                }
            }
        };

        Thread backgroundMailer = new Thread(runnable);
        backgroundMailer.start();
        return MessageManager.MESSAGE_SUCCESS;
    }

    /**
     * Recover password action.
     *
     * @param actionEvent the action event
     */
    public void recoverPasswordAction(ActionEvent actionEvent) {
        int recoverResult = recoverPassword();
        FacesMessage facesMessage = null;
        if (recoverResult == MessageManager.MESSAGE_SUCCESS) {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", "Password is sent on " + user.getEmail());
        } else if (recoverResult == MessageManager.MESSAGE_FAILURE) {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed", "There is no registered user with that phone number/email not found.");
        } else {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "We're sorry. There was an error, please try again.");
        }
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }

    /**
     * Change profile action.
     *
     * @param actionEvent the action event
     */
    public void changeProfileAction(ActionEvent actionEvent) {
        FacesMessage message = null;
        Session session = DAO.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Profile has successfully changed");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (HibernateException dbe) {
            session.getTransaction().rollback();
            dbe.printStackTrace();
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Oops! An error has occurred. Please, try again.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Gets the pin code.
     *
     * @return the pin code
     */
    public String getPinCode() {
        String keyMD5 = user.getGeneratedKey();
        if (keyMD5 == null || keyMD5.trim().equals("")) {
            return "";
        }
        CRC32 keyFactory = new CRC32();
        keyFactory.update(keyMD5.getBytes());
        return String.valueOf(keyFactory.getValue()).substring(0, 4);
    }

    /**
     * Login action.
     *
     * @return the string
     */
    public String loginAction() {
        int loginResult = login();
        if (loginResult == MessageManager.MESSAGE_SUCCESS) {
            return "pretty:mycontacts";
        } else if (loginResult == MessageManager.MESSAGE_FAILURE) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed", "Incorrect phone number or password.");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return "";
        } else {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed", "Oops! An error has occured. Please try again.");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return "";
        }
    }

    /* (non-Javadoc)
     * @see com.pimmanager.beans.UserService#login()
     */
    public Integer login() {
        Session session = DAO.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("from User where phone = :phone and passwd = :passwd");
            q.setString("phone", user.getPhone());
            q.setString("passwd", user.getPassword());
            user = (User) q.uniqueResult();
            if (user != null) {
                user.setLoggedIn(true);
                user.setDateLastLogin(new Date());
                session.beginTransaction();
                session.update(user);
                session.getTransaction().commit();
                return MessageManager.MESSAGE_SUCCESS;
            } else {
                if (session.isOpen()) {
                    session.close();
                }
                logout();
                return MessageManager.MESSAGE_FAILURE;
            }
        } catch (HibernateException dbe) {
            session.getTransaction().rollback();
            return MessageManager.MESSAGE_ERROR;
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Register action.
     *
     * @return the string
     */
    public String registerAction() {
        int resultRegister = register();
        if (resultRegister == MessageManager.MESSAGE_SUCCESS) {
            return "pretty:signup-completed";
        } else {
            return "";
        }
    }

    /* (non-Javadoc)
     * @see com.pimmanager.beans.UserService#register()
     */
    public Integer register() {
        Session session = DAO.getSessionFactory().openSession();
        try {
            if (user.getPassword().compareTo(getAddUserData().getConfirmPassword()) != 0) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation failed", "Passwords are not equal.");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return MessageManager.MESSAGE_FAILURE;
            }
            Query query = session.createQuery("from User where phone = :phone").setString("phone", user.getPhone());
            User u = (User) query.uniqueResult();
            boolean userExists = u != null;
            if (userExists) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registration failed", "There is user already registered with that email address.");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return MessageManager.MESSAGE_FAILURE;
            } else {
                String registrationKey = AppUtils.getUniqueKey();
                user.setGeneratedKey(registrationKey);
                user.setActive(true);
                user.setDateCreated(new Date());
                user.setPaid(false);
                session.beginTransaction();
                session.save(user);
                session.getTransaction().commit();
                if (user.getEmail() != null && !user.getEmail().equals("")) {
                    try {
                        AppUtils.sendEmail(AppUtils.CONTENT_TYPE_HTML, AppConfig.getAdminAutomaticMail(), AppConfig.getAdminName(), user.getEmail(), user.getFirstName() + " " + user.getLastName(), "Welcome to MyBackup", "Welcome, " + user.getFirstName() + "<br><br>Enjoy in our services.");
                    } catch (EmailException ee) {
                    }
                }
                logout();
                return MessageManager.MESSAGE_SUCCESS;
            }
        } catch (HibernateException dbe) {
            session.getTransaction().rollback();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registration failed", "There was an error while processing your request. Please, fill all obligatory fields and try again.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return MessageManager.MESSAGE_FAILURE;
        } finally {
            getAddUserData().clear();
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Change password action.
     *
     * @param actionEvent the action event
     */
    public void changePasswordAction(ActionEvent actionEvent) {
        FacesMessage message = null;
        Session session = DAO.getSessionFactory().openSession();
        try {
            if (getAddUserData().getNewPassword().compareTo(getAddUserData().getConfirmPassword()) != 0) {
                message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Entered passwords are not equal. Repeat your password.");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }
            Query q = session.createQuery("from User where phone = :phone").setString("phone", user.getPhone());
            user = (User) q.uniqueResult();
            String passwordFromDB = user.getPassword();
            if (passwordFromDB == null || passwordFromDB.compareTo(getAddUserData().getOldPassword()) != 0) {
                message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Your old password is not correct.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } else {
                if (getAddUserData().getOldPassword().compareTo(getAddUserData().getNewPassword()) != 0) {
                    user.setPassword(getAddUserData().getNewPassword());
                    session.beginTransaction();
                    session.update(user);
                    session.getTransaction().commit();
                }
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Your password has been changed.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (HibernateException dbe) {
            session.getTransaction().rollback();
            dbe.printStackTrace();
            String messageSummary = "Oops! An error has occurred. Please, try again.";
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", messageSummary);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        } finally {
            getAddUserData().clear();
            if (session.isOpen()) {
                session.close();
            }
        }

    }

    /**
     * Clear add user data.
     *
     * @param actionEvent the action event
     */
    public void clearAddUserData(ActionEvent actionEvent) {
        getAddUserData().clear();
    }

    /**
     * Activate user.
     *
     * @param key the key
     * @return the integer
     */
    public Integer activateUser(long key) {
        Session session = null;
        try {
            CRC32 keyFactory = new CRC32();
            String keyMD5 = user.getGeneratedKey();
            if (keyMD5 == null || keyMD5.trim().equals("")) {
                return MessageManager.MESSAGE_FAILURE;
            }
            keyFactory.update(keyMD5.getBytes());
            String strActivationKeyShort = String.valueOf(keyFactory.getValue()).substring(0, 4);
            long pinCode = Long.parseLong(strActivationKeyShort);
            Date expDate = user.getValidUntil();
            if (key != pinCode || expDate == null || expDate.before(new Date(System.currentTimeMillis()))) {
                return MessageManager.MESSAGE_FAILURE;
            }
            user.setPaid(true);
            session = DAO.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
            return MessageManager.MESSAGE_SUCCESS;
        } catch (HibernateException dbe) {
            session.getTransaction().rollback();
            return MessageManager.MESSAGE_ERROR;
        } finally {
            if (session != null) {
                if (session.isOpen()) {
                    session.close();
                }
            }
        }
    }

    /**
     * Activate user action.
     *
     * @param event the event
     */
    public void activateUserAction(ActionEvent event) {
        try {
            long pin = Long.parseLong(getAddUserData().getPinCode());
            Integer result = activateUser(pin);
            if (result != MessageManager.MESSAGE_SUCCESS) {
                throw new NumberFormatException();
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Activated", "Your account has been activated. Reload page to see changes.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (NumberFormatException nfe) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error during activation", "Entered PIN code is not valid");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    /**
     * On flow process.
     *
     * @param event the event
     * @return the string
     */
    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user.
     *
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the adds the user data.
     *
     * @return the addUserData
     */
    public AdditionalUserData getAddUserData() {
        if (addUserData == null) {
            addUserData = new AdditionalUserData();
        }
        return addUserData;
    }

    /**
     * Sets the adds the user data.
     *
     * @param addUserData the addUserData to set
     */
    public void setAddUserData(AdditionalUserData addUserData) {
        this.addUserData = addUserData;
    }
}
