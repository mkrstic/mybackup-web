/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pimmanager.util;


import com.pimmanager.configuration.AppConfig;
import java.sql.SQLException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
// TODO: Auto-generated Javadoc

/**
 * The listener interface for receiving myContext events.
 * The class that is interested in processing a myContext
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addMyContextListener<code> method. When
 * the myContext event occurs, that object's appropriate
 * method is invoked.
 *
 * @author mladen
 */
public class MyContextListener implements ServletContextListener {


    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce) {
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            java.sql.Driver driver = java.sql.DriverManager.getDriver(AppConfig.getDbConnectionURL());
            java.sql.DriverManager.deregisterDriver(driver);
        } catch (SQLException ex) {
        }
    }

}
