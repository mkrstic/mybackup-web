/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pimmanager.logic;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.pimmanager.beans.User;
import com.pimmanager.util.AppUtils;
import com.pimmanager.util.DAO;

/**
 *
 * @author mladen
 */
@ManagedBean
@RequestScoped
public class UsersList implements Serializable {

    private List<User> users;
    
    /** The user. */
    private User user;
    private int activationErrorCode;

    public UsersList() {
        user = new User();
    }

    /**
     * @return the users
     */
    public List<User> getUsers() {
        if (users == null) {
            Session session = DAO.getSessionFactory().openSession();
            users = new ArrayList<User>();
            try {
                Query q = session.createQuery("from User");
                for (Iterator it = q.iterate(); it.hasNext();) {
                    User u = (User) it.next();
                    if (u.getValidUntil() == null || u.getValidUntil().after(new Date())) {
                        u.setPaid(false);
                        session.beginTransaction();
                        session.update(u);
                        session.getTransaction().commit();
                    }
                    users.add(u);
                }
            } catch (HibernateException ex) {
                session.getTransaction().rollback();
                ex.printStackTrace();
            } finally {
                if (session.isOpen()) {
                    session.close();
                }
            }
        }
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setActive(User user, boolean active) {
        Session session = DAO.getSessionFactory().openSession();
        try {
            user.setActive(active);
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
            String activeStatus = active ? "enabled" : "disabled";
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User with phone no. " + user.getPhone() + " has been " + activeStatus);
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (users != null) {
                for (int i = 0; i < users.size(); i++) {
                    User userLoop = users.get(i);
                    if (userLoop.getId() == user.getId()) {
                        userLoop.setActive(active);
                        break;
                    }
                }
            }
        } catch (HibernateException dbe) {
            session.getTransaction().rollback();
            dbe.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure", "Oops!An error occured. Try again");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    public void delete(User user) {
        Session session = DAO.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.refresh(user);
            session.delete(user);
            session.getTransaction().commit();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User with phone no. " + user.getPhone() + " has been deleted");
            if (users != null) {
                users.remove(user);
            }
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (HibernateException dbe) {
            session.beginTransaction().rollback();
            dbe.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure", "Oops!An error occured. Try again");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    public void deleteAll() {
        Session session = DAO.getSessionFactory().openSession();
        try {
            session.beginTransaction().commit();
            Query q = session.createQuery("delete from User");
            q.executeUpdate();
            session.getTransaction().commit();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "All users deleted.");
            if (users != null) {
                users.clear();
            }
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (HibernateException dbe) {
            dbe.printStackTrace();
            session.getTransaction().rollback();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure", "Oops!An error occured. Try again");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    public String getPinCodeByPhone(String phone) {
        Session session = DAO.getSessionFactory().openSession();
        try {
            String keyMD5 = (String) session.createQuery("select generatedKey from User where phone = :phone").setString("phone", phone).uniqueResult();
            if (keyMD5 == null || keyMD5.trim().equals("")) {
                return "";
            }
            CRC32 keyFactory = new CRC32();
            keyFactory.update(keyMD5.getBytes());
            return String.valueOf(keyFactory.getValue()).substring(0, 4);
        } catch (HibernateException dbe) {
            dbe.printStackTrace();
            return "";
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    public String getPinCode(User u) {
        String keyMD5 = u.getGeneratedKey();
        if (keyMD5 == null || keyMD5.trim().equals("")) {
            return "";
        }
        CRC32 keyFactory = new CRC32();
        keyFactory.update(keyMD5.getBytes());
        return String.valueOf(keyFactory.getValue()).substring(0, 4);
    }

    public int getActivationErrorCode() {
        return activationErrorCode;
    }

    public boolean updateValidUntil(String phone, String strNewValidUntil) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        if (!AppUtils.isPhoneValid(phone)) {
            activationErrorCode = 1;
            return false;
        }
        Date newValidUntil = null;
        Session session = DAO.getSessionFactory().openSession();
        try {
            newValidUntil = formatter.parse(strNewValidUntil);
            session.beginTransaction();
            User user = (User) session.createQuery("from User where phone = :phone").setString("phone", phone).uniqueResult();
            if (user == null) {
                throw new HibernateException("Cannot find user with phone num. '" + phone + "'");
            }
            user.setValidUntil(newValidUntil);
            session.update(user);
            session.getTransaction().commit();
            return true;
        } catch (ParseException pe) {
            activationErrorCode = 2;
            return false;
        } catch (HibernateException dbe) {
            session.getTransaction().rollback();
            activationErrorCode = 3;
            return false;
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    public void updateValidUntil(boolean generateKey) {
        Session session = DAO.getSessionFactory().openSession();
        try {
            if (generateKey) {
                user.setGeneratedKey(AppUtils.getUniqueKey());
            }
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
            if (users != null) {
                for (int i = 0; i < users.size(); i++) {
                    User userLoop = users.get(i);
                    if (userLoop.getId() == user.getId()) {
                        userLoop = user;
                        break;
                    }
                }
            }
        } catch (HibernateException dbe) {
            session.getTransaction().rollback();
            dbe.printStackTrace();
        } finally {
            session.close();
        }
    }

    public boolean findUser() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String strId = request.getParameter("id");
        int id;
        try {
            id = Integer.parseInt(strId);
        } catch (NumberFormatException nfe) {
            return false;
        }
        Session session = DAO.getSessionFactory().openSession();
        try {
        	setUser((User)session.get(User.class, new Integer(id)));
        	if (getUser() == null)
        		return false;
        	return true;
        } catch (HibernateException he) {
        	he.printStackTrace();
        } finally {
        	if (session.isOpen()) session.close();
        }
        return false;
    }

    public void updateProfile() {
        FacesMessage message = null;
        Session session = DAO.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Profile has successfully changed");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (HibernateException dbe) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Oops! An error has occurred. Please, try again.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            dbe.printStackTrace();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    public String countContacts(User u) {
        try {
            int numContacts = user.getContacts().size();
            return String.valueOf(numContacts);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }
}
