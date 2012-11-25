/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pimmanager.beans;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotNull;

// TODO: Auto-generated Javadoc
/**
 * The Class NewsItem.
 *
 * @author mladen
 */
public class NewsItem implements Serializable {

    /** The id. */
    private Integer id;
    
    /** The title. */
    @NotNull(message = "Please enter item title")
    private String title;
    
    /** The author. */
    @NotNull(message = "Please enter author name")
    private String author;
    
    /** The published date. */
    private Date publishedDate;
    
    /** The description. */
    @NotNull(message = "Please enter description")
    private String description;

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Instantiates a new news item.
     *
     * @param title the title
     * @param author the author
     * @param description the description
     * @param publishedDate the published date
     */
    public NewsItem(String title, String author, String description, String publishedDate) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.publishedDate = new Date(Date.parse(publishedDate));
     }

    /**
     * Instantiates a new news item.
     */
    public NewsItem() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Sets the title.
	 *
	 * @param title the title to set
	 */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the author.
     *
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author.
     *
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets the published date.
     *
     * @return the publishedDate
     */
    public Date getPublishedDate() {
        return publishedDate;
    }

    /**
     * Sets the published date.
     *
     * @param publishedDate the publishedDate to set
     */
    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Gets the description filter xml.
     *
     * @param size the size
     * @return the description filter xml
     */
    public String getDescriptionFilterXml(Integer size) {
        String descriptionFiltered = getDescription().replaceAll("\\<.*?>","");
        if (descriptionFiltered.length() > size)
            return descriptionFiltered.substring(0,size);
        else
            return descriptionFiltered;
    }

    /**
     * Sets the description.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
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
}
