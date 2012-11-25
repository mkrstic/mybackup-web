/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pimmanager.configuration;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

// TODO: Auto-generated Javadoc
/**
 * The Class AppConfig.
 *
 * @author mladen
 */
@ManagedBean(name="config")
@RequestScoped
public final class AppConfig implements Serializable {

    /**
     * Instantiates a new app config.
     */
    public AppConfig() {
        
    }
    
    /**
     * Gets the admin email.
     *
     * @return the adminEmail
     */
    public static String getAdminEmail() {
        String value = null;
        try {
            XMLConfiguration xmlConf = new XMLConfiguration(AppConfig.class.getResource("/com/pimmanager/configuration/config.xml"));
            value = xmlConf.getString("admin.email");
        } catch (ConfigurationException ce) {
            ce.printStackTrace();
        }
        return value;
    }

  

    /**
     * Gets the admin name.
     *
     * @return the adminName
     */
    public static String getAdminName() {
        String value = null;
        try {
            XMLConfiguration xmlConf = new XMLConfiguration(AppConfig.class.getResource("/com/pimmanager/configuration/config.xml"));
            value = xmlConf.getString("admin.name");
        } catch (ConfigurationException ce) {
            ce.printStackTrace();
        }
        return value;
    }

    
    /**
     * Gets the smtp host.
     *
     * @return the mailSmtpHost
     */
    public static String getSmtpHost() {
        String value = null;
        try {
            XMLConfiguration xmlConf = new XMLConfiguration(AppConfig.class.getResource("/com/pimmanager/configuration/config.xml"));
            value = xmlConf.getString("smtp.host");
        } catch (ConfigurationException ce) {
            ce.printStackTrace();
        }
        return value;
    }

   
    /**
     * Checks if is smtp auth.
     *
     * @return the mailSmtpAuth
     */
    public static Boolean isSmtpAuth() {
       Boolean value = null;
        try {
            XMLConfiguration xmlConf = new XMLConfiguration(AppConfig.class.getResource("/com/pimmanager/configuration/config.xml"));
            value = xmlConf.getBoolean("smtp.use-auth");
        } catch (ConfigurationException ce) {
            ce.printStackTrace();
        }
        return value;
    }

   
    /**
     * Gets the smtp username.
     *
     * @return the mailSmtpUsername
     */
    public static String getSmtpUsername() {
        String value = null;
        try {
            XMLConfiguration xmlConf = new XMLConfiguration(AppConfig.class.getResource("/com/pimmanager/configuration/config.xml"));
            value = xmlConf.getString("smtp.username");
        } catch (ConfigurationException ce) {
            ce.printStackTrace();
        }
        return value;
    }

    
    /**
     * Gets the smtp password.
     *
     * @return the mailSmtpPassword
     */
    public static String getSmtpPassword() {
        String value = null;
        try {
            XMLConfiguration xmlConf = new XMLConfiguration(AppConfig.class.getResource("/com/pimmanager/configuration/config.xml"));
            value = xmlConf.getString("smtp.password");
        } catch (ConfigurationException ce) {
            ce.printStackTrace();
        }
        return value;
    }

 

    /**
     * Gets the db username.
     *
     * @return the usernameDB
     */
    public static String getDbUsername() {
        String value = null;
        try {
            XMLConfiguration xmlConf = new XMLConfiguration(AppConfig.class.getResource("/com/pimmanager/configuration/config.xml"));
            value = xmlConf.getString("database.username");
        } catch (ConfigurationException ce) {
            ce.printStackTrace();
        }
        return value;
    }

    /**
     * Gets the db password.
     *
     * @return the db password
     */
  

    /**
     * @return the passwordDB
     */
    public static String getDbPassword() {
        String value = null;
        try {
            XMLConfiguration xmlConf = new XMLConfiguration(AppConfig.class.getResource("/com/pimmanager/configuration/config.xml"));
            value = xmlConf.getString("database.password");
        } catch (ConfigurationException ce) {
            ce.printStackTrace();
        }
        return value;
    }

   
    /**
     * Gets the db connection url.
     *
     * @return the connectionURL
     */
    public static String getDbConnectionURL() {
        String value = null;
        try {
            XMLConfiguration xmlConf = new XMLConfiguration(AppConfig.class.getResource("/com/pimmanager/configuration/config.xml"));
            value = xmlConf.getString("database.connection-url");
        } catch (ConfigurationException ce) {
            ce.printStackTrace();
        }
        return value;
    }

    /**
     * Gets the admin automatic mail.
     *
     * @return the noreplyMailAddress
     */
    public static String getAdminAutomaticMail() {
        String value = null;
        try {
            XMLConfiguration xmlConf = new XMLConfiguration(AppConfig.class.getResource("/com/pimmanager/configuration/config.xml"));
            value = xmlConf.getString("smtp.automatic-mail");
        } catch (ConfigurationException ce) {
            ce.printStackTrace();
        }
        return value;
    }

    /**
     * Gets the site url.
     *
     * @return the hostURL
     */
    public static String getSiteUrl() {
        String value = null;
        try {
            XMLConfiguration xmlConf = new XMLConfiguration(AppConfig.class.getResource("/com/pimmanager/configuration/config.xml"));
            value = xmlConf.getString("site-url");
        } catch (ConfigurationException ce) {
            ce.printStackTrace();
        }
        return value;
    }


    /**
     * Gets the smtp port.
     *
     * @return the mailSmtpPort
     */
    public static String getSmtpPort() {
        String value = null;
        try {
            XMLConfiguration xmlConf = new XMLConfiguration(AppConfig.class.getResource("/com/pimmanager/configuration/config.xml"));
            value = xmlConf.getString("smtp.port");
        } catch (ConfigurationException ce) {
            ce.printStackTrace();
        }
        return value;
    }

  

    /**
     * Checks if is smtp debug.
     *
     * @return the mailSmtpDebug
     */
    public static Boolean isSmtpDebug() {
        Boolean value = null;
        try {
            XMLConfiguration xmlConf = new XMLConfiguration(AppConfig.class.getResource("/com/pimmanager/configuration/config.xml"));
            value = xmlConf.getBoolean("smtp.debug-mode");
        } catch (ConfigurationException ce) {
            ce.printStackTrace();
        }
        return value;
    }

  

    /**
     * Gets the admin username.
     *
     * @return the adminUsername
     */
    public static String getAdminUsername() {
        String value = null;
        try {
            XMLConfiguration xmlConf = new XMLConfiguration(AppConfig.class.getResource("/com/pimmanager/configuration/config.xml"));
            value = xmlConf.getString("admin.username");
        } catch (ConfigurationException ce) {
            ce.printStackTrace();
        }
        return value;
    }

  

    /**
     * Gets the admin password.
     *
     * @return the adminPassword
     */
    public static String getAdminPassword() {
       String value = null;
        try {
            XMLConfiguration xmlConf = new XMLConfiguration(AppConfig.class.getResource("/com/pimmanager/configuration/config.xml"));
            value = xmlConf.getString("admin.password");
        } catch (ConfigurationException ce) {
            ce.printStackTrace();
        }
        return value;
    }
}
