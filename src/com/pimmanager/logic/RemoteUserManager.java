/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pimmanager.logic;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.zip.CRC32;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.pimmanager.beans.Contact;
import com.pimmanager.beans.Telephone;
import com.pimmanager.beans.User;
import com.pimmanager.beans.UserService;
import com.pimmanager.beans.exceptions.EmailException;
import com.pimmanager.configuration.AppConfig;
import com.pimmanager.util.AppUtils;
import com.pimmanager.util.DAO;
import com.pimmanager.util.MessageManager;

// TODO: Auto-generated Javadoc
/**
 * The Class RemoteUserManager.
 *
 * @author mladen
 */
public class RemoteUserManager implements UserService, Serializable {

    /**
     * Gets the contact.
     *
     * @param userId the user id
     * @param contactId the contact id
     * @return the contact
     */
    public static Contact getContact(int userId, int contactId) {
        Contact contact = null;
        Session session = DAO.getSessionFactory().openSession();
        try {
            contact = (Contact) session.get(Contact.class, new Integer(contactId));
        } catch (HibernateException dbe) {
            dbe.printStackTrace();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        return contact;
    }

    /**
     * Store contact.
     *
     * @param userId the user id
     * @param contact the contact
     * @return the int
     */
    public static int storeContact(int userId, Contact contact) {
        Session session = DAO.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            User user = (User) session.load(User.class, userId);
            contact.setUser(user);
            user.getContacts().add(contact);
            session.save(contact);
            session.saveOrUpdate(user);
            session.getTransaction().commit();
        } catch (HibernateException dbe) {
            session.getTransaction().rollback();
            return MessageManager.MESSAGE_ERROR;
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        return MessageManager.MESSAGE_SUCCESS;
    }

    /**
     * Find contact id.
     *
     * @param userId the user id
     * @param contactUid the contact uid
     * @return the integer
     */
    public static Integer findContactId(int userId, String contactUid) {
        Integer cId = null;
        Session session = DAO.getSessionFactory().openSession();
        try {
            User user = (User) session.load(User.class, userId);
            Query q = session.createQuery("from Contact where uid = :uid and user = :user");
            q.setString("uid", contactUid);
            q.setEntity("user", user);
            Contact contact = (Contact) q.list().get(0);
            if (contact != null)
            	return contact.getId();
            else
            	return null;
        } catch (HibernateException dbe) {
            dbe.printStackTrace();
            return null;
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Gets the contact revision.
     *
     * @param userId the user id
     * @param contactId the contact id
     * @return the contact revision
     */
    public static Date getContactRevision(Integer userId, int contactId) {
        Session session = DAO.getSessionFactory().openSession();
        try {
            Contact contact = (Contact) session.load(Contact.class, new Integer(contactId));
            return contact.getRevision();
        } catch (HibernateException dbe) {
            dbe.printStackTrace();
            return null;
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Update contact.
     *
     * @param userId the user id
     * @param contact the contact
     * @return the int
     */
    public static int updateContact(int userId, Contact contact) {
        Session session = DAO.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.update(contact);
            session.getTransaction().commit();
        } catch (HibernateException dbe) {
            session.getTransaction().rollback();
            dbe.printStackTrace();
            return MessageManager.MESSAGE_ERROR;
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        return MessageManager.MESSAGE_SUCCESS;
    }

    /**
     * Gets the tels.
     *
     * @param contactId the contact id
     * @return the tels
     */
    public static List<Telephone> getTels(Integer contactId) {
        Contact c = new Contact();
        c.setId(contactId);
        List<Telephone> tels = null;
        Session session = DAO.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("from Telephone where contact = :contact");
            q.setEntity("contact", c);
            tels = q.list();
        } catch (HibernateException he) {
            he.printStackTrace();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        return tels;
    }
    
    /** The user. */
    private User user;

    /**
     * Instantiates a new remote user manager.
     *
     * @param user the user
     */
    public RemoteUserManager(User user) {
        this.user = user;
    }

    /* (non-Javadoc)
     * @see com.pimmanager.beans.UserService#login()
     */
    public Integer login() {
        Session session = DAO.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("from User where phone = :phone and password = :password");
            q.setString("phone", user.getPhone());
            q.setString("password", user.getPassword());
            user = (User) q.uniqueResult();
            if (user != null) {
                user.setLoggedIn(true);
                user.setDateLastLogin(new Date());
                session.beginTransaction();
                session.update(user);
                session.getTransaction().commit();
                return MessageManager.MESSAGE_SUCCESS;
            } else {
                return MessageManager.MESSAGE_FAILURE;
            }

        } catch (HibernateException dbe) {
            dbe.printStackTrace();
            session.getTransaction().rollback();
            return MessageManager.MESSAGE_ERROR;
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    /* (non-Javadoc)
     * @see com.pimmanager.beans.UserService#register()
     */
    public Integer register() {
        Session session = DAO.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("from User where phone = :phone").setString("phone", user.getPhone());
            User u = (User) query.uniqueResult();
            boolean userExists = u != null;
            if (userExists) {
                return MessageManager.MESSAGE_FAILURE;
            } else {
                final String registrationKey = AppUtils.getUniqueKey();
                user.setGeneratedKey(registrationKey);
                user.setActive(true);
                user.setDateCreated(new Date());
                user.setPaid(false);
                session.beginTransaction();
                session.save(user);
                session.getTransaction().commit();
                return MessageManager.MESSAGE_SUCCESS;
            }
        } catch (HibernateException dbe) {
            dbe.printStackTrace();
            session.getTransaction().rollback();
            return MessageManager.MESSAGE_ERROR;
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
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
        boolean isRegistered = (user != null);
        if (!isRegistered) {
            return MessageManager.MESSAGE_FAILURE;
        }
        final String msgSubject = "Password recovery";
        String editableMsgContent;
        try {
            String path = (AppUtils.getWebInfPath() + "emailpages" + java.io.File.separator + "passwordRecovery.html").replace("%20", " ");
            editableMsgContent = AppUtils.readFile(path);
        } catch (IOException ioe) {
            editableMsgContent =
                    "Dear Mr./Ms. <b>[$lastName]"
                    + "</b><p></p><p>Your password is:</p><p>"
                    + "[$password]</p><p>You can now login.</p>"
                    + "<p></p><p>Regards,</p>" + "MyBackup team";
        }
        final String msgContent =
                editableMsgContent.replace("[$firstName]", user.getFirstName()).replace("[$lastName]", user.getLastName()).replace("[$email]", user.getEmail()).replace("[$password]", user.getPassword());
        final User u = user;
        Runnable runnable = new Runnable() {

            public void run() {
                try {
                    AppUtils.sendEmail(AppUtils.CONTENT_TYPE_HTML, AppConfig.getAdminAutomaticMail(), "MyBackup team", u.getEmail(), u.getFirstName() + " " + u.getLastName(), msgSubject, msgContent);
                } catch (EmailException ex) {
                    // nothing for now
                }
            }
        };
        Thread t = new Thread(runnable);
        t.start();
        return MessageManager.MESSAGE_SUCCESS;
    }

    /**
     * Count contacts.
     *
     * @return the int
     */
    public int countContacts() {
        Session session = DAO.getSessionFactory().openSession();
        try {
        	session.refresh(user);
        	Query q = session.createQuery("select count(*) from Contact where user = :user");
        	q.setEntity("user", user);
            return (Integer) q.uniqueResult();
        } catch (HibernateException he) {
            he.printStackTrace();
            return MessageManager.MESSAGE_ERROR;
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Gets the contact ids.
     *
     * @return the contact ids
     */
    public Object[] getContactIds() {
        Session session = DAO.getSessionFactory().openSession();
        List<Integer> contactIds = null;
        try {
        	session.refresh(user);
        	Query q = session.createQuery("select contact.id from Contact where user = :user");
        	q.setEntity("user", user);
            return q.list().toArray();
        } catch (HibernateException dbe) {
            dbe.printStackTrace();
            return null;
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Delete all contacts.
     *
     * @return the boolean
     */
    public Boolean deleteAllContacts() {
        Session session = DAO.getSessionFactory().openSession();
        try {
        	session.refresh(user);
        	session.beginTransaction();
        	Query q = session.createQuery("delete from Contact where user = :user").setEntity("user", user);
        	session.getTransaction().commit();
        	q.executeUpdate();
            user.getContacts().clear();
        } catch (HibernateException dbe) {
        	if (session.getTransaction().isActive())
        		session.getTransaction().rollback();
            dbe.printStackTrace();
            return false;
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        return true;
    }

    /**
     * Gets the contact uids.
     *
     * @return the contact uids
     */
    public Vector<String> getContactUids() {
        Vector<String> contactUids = new Vector<String>();
        Session session = DAO.getSessionFactory().openSession();
        try {
            user = (User) session.load(User.class, user.getId());
            for (Contact c : user.getContacts()) {
                contactUids.add(c.getUid());
            }
        } catch (HibernateException dbe) {
            dbe.printStackTrace();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        return contactUids;
    }

    /**
     * Delete contacts by uid.
     *
     * @param contactUids the contact uids
     * @return the integer
     */
    public Integer deleteContactsByUid(String[] contactUids) {
        Integer deletedNum = 0;
        Session session = DAO.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query q = session.createQuery("delete from Contact where uid in (:uidList)");
            q.setParameterList("uidList", contactUids);
            deletedNum = q.executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException dbe) {
            session.getTransaction().rollback();
            dbe.printStackTrace();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        return deletedNum;
    }

    /**
     * Activate account.
     *
     * @param enteredKey the entered key
     * @return the integer
     */
    public Integer activateAccount(long enteredKey) {
        Session session = DAO.getSessionFactory().openSession();
        try {
            CRC32 keyFactory = new CRC32();
            user = (User) session.load(User.class, user.getId());
            session.beginTransaction();
            String keyMD5 = user.getGeneratedKey();
            if (keyMD5 == null || keyMD5.trim().equals("")) {
                return MessageManager.MESSAGE_FAILURE;
            }
            keyFactory.update(keyMD5.getBytes());
            String strActivationKeyShort = String.valueOf(keyFactory.getValue()).substring(0, 4);
            long pinCode = Long.parseLong(strActivationKeyShort);
            Date expDate = user.getValidUntil();
            if (enteredKey != pinCode || expDate == null || expDate.before(new Date())) {
                return MessageManager.MESSAGE_FAILURE;
            }
            user.setPaid(true);
            session.update(user);
            session.getTransaction().commit();
            return MessageManager.MESSAGE_SUCCESS;
        } catch (HibernateException dbe) {
            dbe.printStackTrace();
            session.getTransaction().rollback();
            return MessageManager.MESSAGE_ERROR;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Update last synced.
     */
    public void updateLastSynced() {
        Session session = DAO.getSessionFactory().openSession();
        try {
            user = (User) session.load(User.class, user.getId());
            Date now = new Date();
            user.setValidUntil(now);
            session.beginTransaction();
            session.update(user);
            session.getTransaction().rollback();
        } catch (HibernateException dbe) {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    /* (non-Javadoc)
     * @see com.pimmanager.beans.UserService#logout()
     */
    public Integer logout() {
        user = null;
        return MessageManager.MESSAGE_SUCCESS;
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
}
