/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pimmanager.logic;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.pimmanager.beans.NewsItem;
import com.pimmanager.configuration.AppConfig;
import com.pimmanager.util.DAO;

// TODO: Auto-generated Javadoc
/**
 * The Class NewsManager.
 *
 * @author mladen
 */
@ManagedBean(name = "newsBean")
@RequestScoped
public class NewsManager implements Serializable {

    /** The news. */
    private List<NewsItem> news;
    
    /** The news item. */
    private NewsItem newsItem;

    /**
     * Instantiates a new news manager.
     */
    public NewsManager() {
        newsItem = new NewsItem();
    }

    /**
     * Clear item.
     *
     * @param event the event
     */
    public void clearItem(ActionEvent event) {
        newsItem.setAuthor("");
        newsItem.setDescription("");
        newsItem.setId(0);
        newsItem.setPublishedDate(null);
        newsItem.setTitle("");
    }

    /**
     * Adds the item.
     */
    public void addItem() {
        Session session = DAO.getSessionFactory().openSession();
        try {
            newsItem.setPublishedDate(new Date());
            newsItem.setAuthor(AppConfig.getAdminName());
            session.beginTransaction();
            session.save(newsItem);
            session.getTransaction().commit();
            clearItem(null);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "News article added", "News article has been added to news list. Note: RSS was not updated");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (HibernateException dbe) {
            session.getTransaction().rollback();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cannot add article, an error occured");
            FacesContext.getCurrentInstance().addMessage(null, message);
            dbe.printStackTrace();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }

    }

    /**
     * Fetch top news.
     *
     * @param maxResults the max results
     * @return the list
     */
    public List<NewsItem> fetchTopNews(int maxResults) {
        if (news == null) {
            Session session = DAO.getSessionFactory().openSession();
            try {
                Query q = session.createQuery("from NewsItem");
                q.setMaxResults(maxResults);
                news = q.list();
            } catch (HibernateException ex) {
                ex.printStackTrace();
            } finally {
                if (session.isOpen()) {
                    session.close();
                }
            }
        }
        return news;
    }

    /**
     * Gets the news.
     *
     * @return the news
     */
    public List<NewsItem> getNews() {
        if (news == null) {
            Session session = DAO.getSessionFactory().openSession();
            try {
                news = session.createQuery("from NewsItem").list();
            } catch (HibernateException ex) {
                ex.printStackTrace();
            } finally {
                if (session.isOpen()) {
                    session.close();
                }
            }
        }
        return news;
    }

    /**
     * Sets the news.
     *
     * @param news the news to set
     */
    public void setNews(List<NewsItem> news) {
        this.news = news;
    }

    /**
     * Delete.
     *
     * @param newsItem the news item
     */
    public void delete(NewsItem newsItem) {
        Session session = DAO.getSessionFactory().openSession();
        try {
            System.out.println(newsItem.getId() + ", " + newsItem.getTitle());
            session.beginTransaction();
            session.delete(newsItem);
            session.getTransaction().commit();
            //session.delete("select newsItem from NewsItem as newsItem where id = " + newsItem.getId());
            if (news != null) {
                news.remove(newsItem);
            }
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Deletion success", "News item has been deleted.");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);

        } catch (HibernateException dbe) {
            dbe.printStackTrace();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Deletion failed", dbe.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }

    }

    /**
     * Update.
     *
     * @param newsItem the news item
     */
    public void update(NewsItem newsItem) {
        Session session = DAO.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.update(newsItem);
            session.getTransaction().commit();
            if (news != null) {
                news.remove(newsItem);
                for (int i = 0; i < news.size(); i++) {
                    NewsItem item = news.get(i);
                    if (item.getId() == newsItem.getId()) {
                        news.set(i, newsItem);
                        break;
                    }
                }
            }
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Update success", "News item has been updated.");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);

        } catch (HibernateException dbe) {
            session.getTransaction().rollback();
            dbe.printStackTrace();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Update failed", dbe.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Delete all.
     */
    public void deleteAll() {
        Session session = DAO.getSessionFactory().openSession();
        try {
            for (Iterator it = session.createQuery("from NewsItem").iterate(); it.hasNext();) {
                NewsItem item = (NewsItem) it.next();
                session.delete(item);
            }
            if (news != null) {
                news.clear();
            }
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Deletion success", "News items has been deleted.");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);

        } catch (HibernateException dbe) {
            dbe.printStackTrace();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Deletion failed", "Oops!Cannot delete news. Try again.");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Find item.
     *
     * @return true, if successful
     */
    public boolean findItem() {
        getNews();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String day = request.getParameter("day");
        String formattedTitle = request.getParameter("title");
        for (NewsItem item : news) {
            //Date publishedDate = new Date( Date.parse(item.getPublishedDate()));
            Date publishedDate = item.getPublishedDate();
            boolean yearEqual = formatDate(publishedDate, "yyyy").equals(year);
            boolean monthEqual = formatDate(publishedDate, "MM").equals(month);
            boolean dayEqual = formatDate(publishedDate, "dd").equals(day);
            boolean titleEqual = item.getTitle().toLowerCase().replace(" ", "-").equals(formattedTitle);
            if (yearEqual && monthEqual && dayEqual && titleEqual) {
                newsItem = item;
                return true;
            }
        }
        return false;

    }

    /**
     * Format date.
     *
     * @param date the date
     * @param format the format
     * @return the string
     */
    public String formatDate(Date date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } catch (Exception ex) {
            if (date != null) {
                return date.toString();
            } else {
                return "";
            }
        }



    }

    /**
     * Gets the news item.
     *
     * @return the newsItem
     */
    public NewsItem getNewsItem() {
        return newsItem;
    }

    /**
     * Sets the news item.
     *
     * @param newsItem the newsItem to set
     */
    public void setNewsItem(NewsItem newsItem) {
        this.newsItem = newsItem;
    }
}
