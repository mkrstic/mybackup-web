/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pimmanager.beans;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class Telephone.
 *
 * @author mladen
 */
public class Telephone implements Serializable {

    /** The telephoneid. */
    private Integer telephoneid;
    
    /** The contact. */
    private Contact contact;
    
    /** The number. */
    private String number;
    
    /** The type. */
    private String type;
    
    /** The preferred. */
    private Boolean preferred = false;

    /**
     * Instantiates a new telephone.
     *
     * @param number the number
     * @param type the type
     * @param preferred the preferred
     */
    public Telephone(String number, String type, boolean preferred) {
        this.number = number;
        this.type = type;
        this.preferred = preferred;

    }

    /**
     * Instantiates a new telephone.
     *
     * @param contact the contact
     * @param number the number
     * @param type the type
     * @param preferred the preferred
     */
    public Telephone(Contact contact, String number, String type, Boolean preferred) {
        this.contact = contact;
        this.number = number;
        this.type = type;
        this.preferred = preferred;
    }

    /**
     * Instantiates a new telephone.
     *
     * @param contact the contact
     * @param number the number
     */
    public Telephone(Contact contact, String number) {
        this.contact = contact;
        this.number = number;
    }

    /**
     * Instantiates a new telephone.
     */
    public Telephone() {
    }

    /**
     * Gets the telephoneid.
     *
     * @return the telephoneid
     */
    public Integer getTelephoneid() {
        return this.telephoneid;
    }

    /**
     * Sets the telephoneid.
     *
     * @param telephoneid the new telephoneid
     */
    public void setTelephoneid(Integer telephoneid) {
        this.telephoneid = telephoneid;
    }

    /**
     * Gets the contact.
     *
     * @return the contact
     */
    public Contact getContact() {
        return this.contact;
    }

    /**
     * Sets the contact.
     *
     * @param contact the new contact
     */
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    /**
     * Gets the number.
     *
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the number.
     *
     * @param number the number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Checks if is preferred.
     *
     * @return the preferred
     */
    public Boolean isPreferred() {
        return preferred;
    }

    /**
     * Sets the preferred.
     *
     * @param preferred the preferred to set
     */
    public void setPreferred(Boolean preferred) {
        this.preferred = preferred;
    }

    /*@Override
    public boolean equals(Object o) {
    if (!(o instanceof Telephone)) {
    return false;
    }
    Telephone other = (Telephone) o;
    if (this.number.equals(other.number)
    && this.preferred == other.preferred
    && this.type.equals(other.type)) {
    return true;
    } else {
    return false;
    }
    }

    @Override
    public int hashCode() {
    int hash = 3;
    hash = 29 * hash + (this.number != null ? this.number.hashCode() : 0);
    return hash;
    }*/
}
