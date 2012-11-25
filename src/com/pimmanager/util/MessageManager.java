/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pimmanager.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageManager.
 */
public class MessageManager {


    /** The instance. */
    private static MessageManager instance;
    
    /** The MESSAG e_ success. */
    public static int MESSAGE_SUCCESS = 0;
    
    /** The MESSAG e_ failure. */
    public static int MESSAGE_FAILURE = 1;
    
    /** The MESSAG e_ error. */
    public static int MESSAGE_ERROR = -1;

    /** The resource bundle. */
    private ResourceBundle resourceBundle;

    /**
     * Instantiates a new message manager.
     */
    private MessageManager() {

    }

    /**
     * Gets the.
     *
     * @return the message manager
     */
    public static MessageManager get() {
        if (instance == null) {
            instance = new MessageManager();
        }
        return instance;
    }


    /**
     * Gets the message.
     *
     * @param key the key
     * @return the message
     */
    public String getMessage(String key) {
        return getMessageFromJSFBundle(key);
    }

    /**
     * Gets the message from resource bundle.
     *
     * @param key the key
     * @return the message from resource bundle
     */
    private String getMessageFromResourceBundle(String key) {
        String message = "";
        try {
            message = getResourceBundle().getString(key);
        } catch (Exception e) {
        }
        return message;

    }


    /**
     * Gets the message from jsf bundle.
     *
     * @param key the key
     * @return the message from jsf bundle
     */
    private String getMessageFromJSFBundle(String key) {
        return (String)resolveExpression("#{msg['" + key + "']}");
    }

    /**
     * Gets the current loader.
     *
     * @param fallbackClass the fallback class
     * @return the current loader
     */
    private ClassLoader getCurrentLoader(Object fallbackClass) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null)
            loader = fallbackClass.getClass().getClassLoader();
        return loader;
    }

    // from JSFUtils in Oracle ADF 11g Storefront Demo
    /**
     * Resolve expression.
     *
     * @param expression the expression
     * @return the object
     */
    private Object resolveExpression(String expression) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application app = facesContext.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        ValueExpression valueExp = elFactory.createValueExpression(elContext, expression, Object.class);
        return valueExp.getValue(elContext);
    }

    /**
     * Gets the resource bundle.
     *
     * @return the resource bundle
     */
    private ResourceBundle getResourceBundle() {
        if (resourceBundle == null) {
            String bundleName = "com.pimmanager.configuration.Messages";
            Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            try {
                resourceBundle = ResourceBundle.getBundle(bundleName, locale, getCurrentLoader(bundleName));
            } catch (MissingResourceException mre) {
            }
        }
        return resourceBundle;
        
    }
}
