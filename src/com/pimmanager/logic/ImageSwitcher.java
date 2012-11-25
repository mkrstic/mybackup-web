/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pimmanager.logic;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

// TODO: Auto-generated Javadoc
/**
 * The Class ImageSwitcher.
 *
 * @author mladen
 */
@ManagedBean(name="imageSwitchBean")
@RequestScoped
public class ImageSwitcher implements Serializable{
    
    /**
     * Instantiates a new image switcher.
     */
    public ImageSwitcher() {
        
    }
    

    /**
     * Gets the screens counter.
     *
     * @param n the n
     * @return the images
     */
    public List<Integer> getScreensCounter(int n) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            list.add(i);
        }
        return list;
    }
    
    /**
     * Images left panel.
     *
     * @return the list
     */
    public List<String> imagesLeftPanel() {
        List<String> images = new ArrayList<String>();
        for (int i = 1; i <= 4; i++) {
            String imageName = "business_people_lp" + i + ".jpg";
            images.add(imageName);
        }
        return images;
    }
    
    /**
     * Images screenshots.
     *
     * @return the list
     */
    public List<String> imagesScreenshots() {
        List<String> images = new ArrayList<String>();
        for (int i = 1; i <= 8; i++) {
            String imageName = "mybackup_screens" + i + ".jpg";
            images.add(imageName);
        }
        return images;
    }
    
}
class PairImageName {
    private String bigImage;
    private String smallImage;
    private String title;
    
    private Object evaluateEL(String expression) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();
        ExpressionFactory exprFactory = app.getExpressionFactory();
        ELContext elContext = fc.getELContext();
        ValueExpression valExpr = exprFactory.createValueExpression(elContext, expression, Object   .class);
        return valExpr.getValue(elContext);
    }
    
    public String resourceValueBigImage() {
        String result = "";
        try {
          String expression = "#{resource['images:$imageFileName']}".replace("$imageFileName", bigImage);
           result = (String) evaluateEL(expression);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * @return the bigImage
     */
    public String getBigImage() {
        return bigImage;
    }

    /**
     * @param bigImage the bigImage to set
     */
    public void setBigImage(String bigImage) {
        this.bigImage = bigImage;
    }

    /**
     * @return the smallImage
     */
    public String getSmallImage() {
        return smallImage;
    }

    /**
     * @param smallImage the smallImage to set
     */
    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
