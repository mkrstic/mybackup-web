/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pimmanager.validation;

import com.pimmanager.util.AppUtils;
import com.pimmanager.util.MessageManager;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.primefaces.component.password.Password;

/**
 *
 * @author mladen
 */
public class MyConfirmPasswordValidator implements Validator {

    /* (non-Javadoc)
     * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
     */
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String confirmedValue = (String) value;
        Password passComponent = (Password) AppUtils.findComponent(component.getParent(), "registerPassword");
        try {
            String passValue = (String) passComponent.getValue();
            if (passValue.compareTo(confirmedValue) != 0) {
                String messageSummary = MessageManager.get().getMessage("passwordsNotSameMessage");
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageSummary, messageSummary);
                throw new ValidatorException(message);
            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            
        }


        
    }
}
