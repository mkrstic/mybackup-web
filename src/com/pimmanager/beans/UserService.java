/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pimmanager.beans;

// TODO: Auto-generated Javadoc
/**
 * The Interface UserService.
 *
 * @author mladen
 */
public interface UserService {
    
    /**
     * Login.
     *
     * @return the integer
     */
    public Integer login();
    
    /**
     * Logout.
     *
     * @return the integer
     */
    public Integer logout();
    
    /**
     * Register.
     *
     * @return the integer
     */
    public Integer register();
    
    /**
     * Recover password.
     *
     * @return the integer
     */
    public Integer recoverPassword();
}
