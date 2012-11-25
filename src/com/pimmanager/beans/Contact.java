/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pimmanager.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.URL;

// TODO: Auto-generated Javadoc
/**
 * The Class Contact.
 *
 * @author mladen
 */
public class Contact implements Serializable {

    /** The id. */
    private Integer id;
    
    /** The user. */
    private User user;
    
    /** The name. */
    private String name;
	
	/** The telephones. */
	private Set<Telephone> telephones = new HashSet(0);
    
    /** The url. */
    @URL
    private String url;
    
    /** The email. */
    @Email
    private String email;
    
    /** The address. */
    private String address;
    
    /** The note. */
    private String note;
    
    /** The revision. */
    @Past
    private Date revision;
    
    /** The uid. */
    private String uid;

    /**
     * Instantiates a new contact.
     */
    public Contact() {
    }

    /**
     * Instantiates a new contact.
     *
     * @param id the id
     * @param name the name
     * @param telephones the telephones
     * @param uid the uid
     */
    public Contact(Integer id, String name, Set<Telephone> telephones, String uid) {
        this.id = id;
        this.telephones = telephones;
        this.name = name;
        this.uid = uid;
    }
    
    /**
     * Instantiates a new contact.
     *
     * @param user the user
     * @param name the name
     * @param email the email
     * @param url the url
     * @param address the address
     * @param note the note
     * @param revision the revision
     * @param uid the uid
     * @param telephones the telephones
     */
    public Contact(User user, String name, String email, String url, String address, String note, Date revision, String uid, Set telephones) {
        this.user = user;
        this.name = name;
        this.email = email;
        this.url = url;
        this.address = address;
        this.note = note;
        this.revision = revision;
        this.uid = uid;
        this.telephones = telephones;
     }

    /**
     * Gets the user.
     *
     * @return the user
     */
    public User getUser() {
        return this.user;
    }
    
    /**
     * Sets the user.
     *
     * @param user the new user
     */
    public void setUser(User user) {
        this.user = user;
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
     * Gets the url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the url.
     *
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets the note.
     *
     * @return the description
     */
    public String getNote() {
        return note;
    }

    /**
     * Sets the note.
     *
     * @param note the new note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

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
     * Gets the address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address.
     *
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the revision.
     *
     * @return the revision
     */
    public Date getRevision() {
        return revision;
    }

    /**
     * Sets the revision.
     *
     * @param revision the revision to set
     */
    public void setRevision(Date revision) {
        this.revision = revision;
    }

    /**
     * Gets the uid.
     *
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * Sets the uid.
     *
     * @param uid the uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Gets the telephones.
     *
     * @return the telephones
     */
    public Set<Telephone> getTelephones() {
        return telephones;
    }

    /**
     * Sets the telephones.
     *
     * @param telephones the telephones to set
     */
    public void setTelephones(Set<Telephone> telephones) {
        this.telephones = telephones;
    }
}
