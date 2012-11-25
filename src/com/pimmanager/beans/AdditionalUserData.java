/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pimmanager.beans;

import java.io.Serializable;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

// TODO: Auto-generated Javadoc
/**
 * The Class AdditionalUserData.
 *
 * @author mladen
 */
public class AdditionalUserData implements Serializable {

    /** The registration key. */
    private String registrationKey; // generated key during registration
    
    /** The confirm password. */
    private String confirmPassword;
    
    /** The old password. */
    private String oldPassword;
    
    /** The new password. */
    private String newPassword;
    
    /** The pin code. */
    @NotEmpty
    @Length(min = 3, max = 4, message = "Entered key is not valid")
    private String pinCode;

    /**
     * Instantiates a new additional user data.
     */
    public AdditionalUserData() {
        clear();
    }

    /**
     * Gets the confirm password.
     *
     * @return the confirm password
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Sets the confirm password.
     *
     * @param confirmPassword the new confirm password
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * Gets the new password.
     *
     * @return the new password
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Sets the new password.
     *
     * @param newPassword the new new password
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * Gets the old password.
     *
     * @return the old password
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * Sets the old password.
     *
     * @param oldPassword the new old password
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    /**
     * Gets the pin code.
     *
     * @return the pin code
     */
    public String getPinCode() {
        return pinCode;
    }

    /**
     * Sets the pin code.
     *
     * @param pinCode the new pin code
     */
    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    /**
     * Gets the registration key.
     *
     * @return the registration key
     */
    public String getRegistrationKey() {
        return registrationKey;
    }

    /**
     * Sets the registration key.
     *
     * @param registrationKey the new registration key
     */
    public void setRegistrationKey(String registrationKey) {
        this.registrationKey = registrationKey;
    }

    /**
     * Clear.
     */
    public void clear() {
        setRegistrationKey("");
        setConfirmPassword("");
        setNewPassword("");
        setOldPassword("");
        setPinCode("");
        setRegistrationKey("");
    }
}
