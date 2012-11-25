/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pimmanager.logic;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.primefaces.event.SelectEvent;

import com.pimmanager.beans.Contact;
import com.pimmanager.beans.Telephone;
import com.pimmanager.beans.User;
import com.pimmanager.util.DAO;
import java.util.ArrayList;
import org.hibernate.Session;

// TODO: Auto-generated Javadoc
/**
 * The Class ContactsContainer.
 *
 * @author mladen
 */
@ManagedBean(name = "contactsStack")
@RequestScoped
public class ContactsContainer implements Serializable {

    /** The selected contact. */
    private Contact selectedContact;
    
    /** The contacts. */
    private List<Contact> contacts;

    /**
     * Gets the selected contact.
     *
     * @return the selectedContact
     */
    public Contact getSelectedContact() {
        return selectedContact;
    }

    /**
     * Sets the selected contact.
     *
     * @param selectedContact the selectedContact to set
     */
    public void setSelectedContact(Contact selectedContact) {
        this.selectedContact = selectedContact;
    }

    /**
     * Gets the contacts.
     *
     * @return the contacts
     */
    public List<Contact> getContacts() {
        return contacts;
    }

    /**
     * Fetch telephones.
     *
     * @param id the id
     * @return the list
     */
    public List<Telephone> fetchTelephones(int id) {
        if (id == 0) {
            return new ArrayList<Telephone>(0);
        }
        Session session = DAO.getSessionFactory().openSession();
        List<Telephone> telephones = null;
        try {
            Contact contact = (Contact) session.load(Contact.class, new Integer(id));
            telephones = new ArrayList<Telephone>(contact.getTelephones());
        } catch (HibernateException he) {
            he.printStackTrace();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        return telephones;
    }

    /**
     * Fetch contacts.
     *
     * @param user the user
     * @return the list
     */
    public List<Contact> fetchContacts(User user) {
        if (contacts == null) {
            Session session = DAO.getSessionFactory().openSession();
            try {
                user = (User) session.load(User.class, user.getId());
                contacts = new ArrayList<Contact>(user.getContacts());
            } catch (HibernateException dbe) {
                dbe.printStackTrace();
            } finally {
                if (session.isOpen()) {
                    session.close();
                }
            }
        }
        return contacts;
    }

    /**
     * Sets the contacts.
     *
     * @param contacts the contacts to set
     */
    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    /**
     * Delete contact.
     *
     * @param u the user
     * @param c the contact
     */
    public void delete(User u, Contact c) {
        FacesMessage facesMessage = null;
        Session session = DAO.getSessionFactory().openSession();
        Transaction tx = null;
        try {
        	u.getContacts().remove(c);
        	if (contacts != null) {
                contacts.remove(c);
            }
        	tx = session.beginTransaction();
        	session.delete(c);
        	tx.commit();
        	tx = null;
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Deletion success", "Contact has been deleted");
        } catch (HibernateException dbe) {
        	if (tx != null) tx.rollback();
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Deletion failed", "An error occured. Cannot delete contact");
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }

    /**
     * On row select navigate.
     *
     * @param event the event
     * @return the string
     */
    public String onRowSelectNavigate(SelectEvent event) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("selectedContact", event.getObject());
        return "contactDetail?faces-redirect=false";
    }
}
