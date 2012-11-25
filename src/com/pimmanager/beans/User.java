/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pimmanager.beans;

/**
 *
 * @author mladen
 */
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

// TODO: Auto-generated Javadoc
/**
 * The Class User.
 */
public class User implements Serializable {

    /** The id. */
    private Integer id;
    
    /** The phone. */
    @NotEmpty
    @Length(min = 5, max = 20)
    private String phone;
    
    /** The password. */
    @NotEmpty
    @Length(min = 5, max = 20)
    private String password;
    
    /** The email. */
    @Email(message = "Entered e-mail address is not valid")
    private String email;
    
    /** The first name. */
    @Length(max = 45)
    private String firstName;
    
    /** The last name. */
    @Length(max = 45)
    private String lastName;
    
    /** The town. */
    @Length(max = 45)
    private String town;
    
    /** The gender. */
    private String gender;
    
    /** The paid. */
    private Boolean paid;
    
    /** The active. */
    private Boolean active;
    
    /** The date last login. */
    private Date dateLastLogin;
    
    /** The date created. */
    private Date dateCreated;
    
    /** The generated key. */
    private String generatedKey;
    
    /** The logged in. */
    private Boolean loggedIn = false;
    
    /** The contacts. */
    private Set<Contact> contacts;
    
    /** The last synced. */
    private Date lastSynced;
    
    /** The birth date. */
    private Date birthDate;
    
    /** The valid until. */
    private Date validUntil;

    /**
     * Instantiates a new user.
     *
     * @param phone the phone
     * @param password the password
     */
    public User(String phone, String password) {

        this.phone = phone;
        this.password = password;
    }

    /**
     * Instantiates a new user.
     */
    public User() {
    }

    /**
     * Instantiates a new user.
     *
     * @param phone the phone
     * @param passwd the passwd
     * @param firstName the first name
     * @param lastName the last name
     * @param email the email
     * @param town the town
     * @param birthDate the birth date
     * @param gender the gender
     * @param paid the paid
     * @param active the active
     * @param dateLastLogin the date last login
     * @param dateCreated the date created
     * @param lastSynced the last synced
     * @param generatedKey the generated key
     * @param validUntil the valid until
     * @param contacts the contacts
     */
    public User(String phone, String passwd, String firstName, String lastName, String email, String town, Date birthDate, String gender, Boolean paid, Boolean active, Date dateLastLogin, Date dateCreated, Date lastSynced, String generatedKey, Date validUntil, Set contacts) {
        this.phone = phone;
        this.password = passwd;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.town = town;
        this.birthDate = birthDate;
        this.gender = gender;
        this.paid = paid;
        this.active = active;
        this.dateLastLogin = dateLastLogin;
        this.dateCreated = dateCreated;
        this.lastSynced = lastSynced;
        this.generatedKey = generatedKey;
        this.validUntil = validUntil;
        this.contacts = contacts;
    }

    /**
     * Instantiates a new user.
     *
     * @param id the id
     */
    public User(Integer id) {
        this.id = id;
    }

    /**
     * Clear.
     */
    public void clear() {
        if (phone != null) {
            setPhone("");
        }
        if (password != null) {
            setPassword("");
        }
        if (firstName != null) {
            setFirstName("");
        }
        if (lastName != null) {
            setLastName("");
        }
        if (email != null) {
            setEmail("");
        }
        if (town != null) {
            setTown("");
        }
        if (birthDate != null) {
            setBirthDate(null);
        }
        if (gender != null) {
            setGender("");
        }
        if (paid != null) {
            setPaid(false);
        }
        if (active != null) {
            setActive(false);
        }
        if (dateLastLogin != null) {
            setDateLastLogin(null);
        }
        if (dateCreated != null) {
            setDateCreated(null);
        }
        if (lastSynced != null) {
            setLastSynced(null);
        }
        if (generatedKey != null) {
            setGeneratedKey("");
        }
        if (validUntil != null) {
            setValidUntil(null);
        }
        if (contacts != null) {
            contacts.clear();
        }
    }

    /**
     * Gets the generated key.
     *
     * @return the generated key
     */
    public String getGeneratedKey() {
        return generatedKey;
    }

    /**
     * Sets the generated key.
     *
     * @param generatedKey the new generated key
     */
    public void setGeneratedKey(String generatedKey) {
        this.generatedKey = generatedKey;
    }

    /**
     * Checks if is logged in.
     *
     * @return the boolean
     */
    public Boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Sets the logged in.
     *
     * @param loggedIn the new logged in
     */
    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    /**
     * Gets the first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     *
     * @param firstName the new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     *
     * @param lastName the new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
     * @param email the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the phone.
     *
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone.
     *
     * @param phone the new phone
     */
    public void setPhone(String phone) {
        //this.phone = AppUtils.normalizePhoneNumber(phone);
        this.phone = phone;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
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
     * Gets the town.
     *
     * @return the town
     */
    public String getTown() {
        return town;
    }

    /**
     * Sets the town.
     *
     * @param town the new town
     */
    public void setTown(String town) {
        this.town = town;
    }

    /**
     * Checks if is paid.
     *
     * @return the boolean
     */
    public Boolean isPaid() {
        return paid;
    }

    /**
     * Gets the paid.
     *
     * @return the paid
     */
    public Boolean getPaid() {
        return paid;
    }

    /**
     * Gets the logged in.
     *
     * @return the logged in
     */
    public Boolean getLoggedIn() {
        return loggedIn;
    }

    /**
     * Sets the paid.
     *
     * @param paid the new paid
     */
    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    /**
     * Gets the date last login.
     *
     * @return the date last login
     */
    public Date getDateLastLogin() {
        return dateLastLogin;
    }

    /**
     * Sets the date last login.
     *
     * @param dateLastLogin the new date last login
     */
    public void setDateLastLogin(Date dateLastLogin) {
        this.dateLastLogin = dateLastLogin;
    }

    /**
     * Gets the date created.
     *
     * @return the date created
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * Sets the date created.
     *
     * @param dateCreated the new date created
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * Gets the contacts.
     *
     * @return the contacts
     */
    public Set<Contact> getContacts() {
        return contacts;
    }

    /**
     * Sets the contacts.
     *
     * @param contacts the new contacts
     */
    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }

    /**
     * Checks if is active.
     *
     * @return the active
     */
    public Boolean isActive() {
        return active;
    }

    /**
     * Gets the active.
     *
     * @return the active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * Sets the active.
     *
     * @param active the active to set
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * Gets the birth date.
     *
     * @return the birthDate
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the birth date.
     *
     * @param birthDate the birthDate to set
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Gets the gender.
     *
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender.
     *
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets the last synced.
     *
     * @return the lastSynced
     */
    public Date getLastSynced() {
        return lastSynced;
    }

    /**
     * Sets the last synced.
     *
     * @param lastSynced the lastSynced to set
     */
    public void setLastSynced(Date lastSynced) {
        this.lastSynced = lastSynced;
    }

    /**
     * Gets the valid until.
     *
     * @return the validUntil
     */
    public Date getValidUntil() {
        return validUntil;
    }

    /**
     * Sets the valid until.
     *
     * @param validUntil the validUntil to set
     */
    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }
}
