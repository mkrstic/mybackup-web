/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pimmanager.beans;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class PhoneDevice.
 *
 * @author mladen
 */
public class PhoneDevice implements Serializable {
    
    /** The id. */
    private Integer id;
    
    /** The vendor. */
    private String vendor;
    
    /** The model. */
    private String model;
    
    /** The image file path. */
    private String imageFilePath;

    /**
     * Instantiates a new phone device.
     */
    public PhoneDevice() {
    }

    /**
     * Instantiates a new phone device.
     *
     * @param id the id
     */
    public PhoneDevice(Integer id) {
        this.id = id;
    }

    /**
     * Instantiates a new phone device.
     *
     * @param id the id
     * @param vendor the vendor
     * @param model the model
     * @param imageFilePath the image file path
     */
    public PhoneDevice(Integer id, String vendor, String model, String imageFilePath) {
        this.id = id;
        this.vendor = vendor;
        this.model = model;
        this.imageFilePath = imageFilePath;
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
     * @param id the new id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the model.
     *
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model.
     *
     * @param model the new model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Gets the image file path.
     *
     * @return the image file path
     */
    public String getImageFilePath() {
        return imageFilePath;
    }

    /**
     * Sets the image file path.
     *
     * @param imageFilePath the new image file path
     */
    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    /**
     * Gets the vendor.
     *
     * @return the vendor
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * Sets the vendor.
     *
     * @param vendor the new vendor
     */
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
    

}
