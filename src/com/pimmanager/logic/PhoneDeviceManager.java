/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pimmanager.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.pimmanager.beans.PhoneDevice;
import com.pimmanager.util.DAO;

// TODO: Auto-generated Javadoc
/**
 * The Class PhoneDeviceManager.
 *
 * @author mladen
 */
@ManagedBean(name = "phoneDeviceBean")
@RequestScoped
public class PhoneDeviceManager implements Serializable {

    /** The phone. */
    private PhoneDevice phone;
    
    /** The text. */
    private String text;

    /**
     * Instantiates a new phone device manager.
     */
    public PhoneDeviceManager() {
        phone = new PhoneDevice();
    }

    /**
     * Check supported action.
     */
    public void checkSupportedAction() {
        FacesMessage message;
        if (isSupported(phone)) {
            message = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Operation succed",
                    "Good news! Your phone is on supported list.");
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Operation succed",
                    "We're sorry, but your phone is not supported yet.");
            phone.setModel("");
            phone.setVendor("");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /**
     * List supported phones.
     *
     * @return the list
     */
    public List<PhoneDevice> listSupportedPhones() {
        List<PhoneDevice> phones = new ArrayList<PhoneDevice>();
        Session session = DAO.getSessionFactory().openSession();
        try {
            phones = session.createQuery("from PhoneDevice").list();
        } catch (HibernateException dbe) {
            dbe.printStackTrace();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        return phones;
    }

    /**
     * Checks if is supported.
     *
     * @param phone the phone
     * @return true, if is supported
     */
    public boolean isSupported(PhoneDevice phone) {
        if (phone.getModel().trim().equals("") || phone.getVendor().trim().equals("")) {
            return false;
        }
        Session session = DAO.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("from PhoneDevice where vendor = :vendor and model = :model");
            q.setString("vendor", phone.getVendor());
            q.setString("model", phone.getModel());
            return (q.uniqueResult() != null);
        } catch (HibernateException dbe) {
            dbe.printStackTrace();
            return false;
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Gets the phone.
     *
     * @return the phone
     */
    public PhoneDevice getPhone() {
        return phone;
    }

    /**
     * Sets the phone.
     *
     * @param phone the new phone
     */
    public void setPhone(PhoneDevice phone) {
        this.phone = phone;
    }

    /**
     * Gets the text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text.
     *
     * @param text the new text
     */
    public void setText(String text) {
        this.text = text;
    }
}
