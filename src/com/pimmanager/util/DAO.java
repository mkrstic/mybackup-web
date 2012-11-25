/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pimmanager.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.pimmanager.configuration.AppConfig;

// TODO: Auto-generated Javadoc
/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 * 
 * @author mladen
 */
public class DAO {

    /** The Constant sessionFactory. */
    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new AnnotationConfiguration().setProperty("hibernate.connection.username",
                    AppConfig.getDbUsername()).setProperty("hibernate.connection.password",
                    AppConfig.getDbPassword()).setProperty("hibernate.connection.url",
                    AppConfig.getDbConnectionURL()).configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Log the exception.
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Gets the session factory.
     *
     * @return the session factory
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

   
    
}
