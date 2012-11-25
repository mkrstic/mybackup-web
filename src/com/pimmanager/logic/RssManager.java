/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pimmanager.logic;

import java.io.FileWriter;
import java.io.Serializable;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.pimmanager.beans.NewsItem;
import com.pimmanager.configuration.AppConfig;
import com.pimmanager.util.DAO;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.SyndFeedOutput;

// TODO: Auto-generated Javadoc
/**
 * The Class RssManager.
 *
 * @author mladen
 */
@ManagedBean(name = "rssBean")
@RequestScoped
public class RssManager implements Serializable {

    /**
     * Update rss.
     *
     * @param event the event
     */
    public void updateRss(ActionEvent event) {
        updateRss();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "RSS feed updated", "RSS feed updated");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /**
     * Update rss.
     */
    public void updateRss() {
        Session session = DAO.getSessionFactory().openSession();
        try {
            String feedType = "rss_2.0";
            String fileName = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/files/feed.xml");

            SyndFeed feed = new SyndFeedImpl();
            feed.setFeedType(feedType);

            feed.setTitle("MyBackup news");
            feed.setAuthor(AppConfig.getAdminName());
            feed.setLink(AppConfig.getSiteUrl());
            feed.setDescription("MyBackup news feed.");
            List<NewsItem> news = session.createQuery("from NewsItem").list();
            List<SyndEntry> entries = new ArrayList<SyndEntry>(news.size());
            for (NewsItem newsItem : news) {
                SyndEntry entry = new SyndEntryImpl();
                String itemURL = calculateURL(newsItem);
                entry.setUri(itemURL);
                entry.setTitle((newsItem.getTitle()));
                entry.setAuthor(newsItem.getAuthor());
                //entry.setPublishedDate(new Date(Date.parse(newsItem.getPublishedDate())));
                SyndContent entryDescription = new SyndContentImpl();
                entryDescription.setType("text/html");
                entryDescription.setValue(newsItem.getDescription());
                entry.setDescription(entryDescription);
                entries.add(entry);
            }
            feed.setEntries(entries);

            Writer writer = new FileWriter(fileName);
            SyndFeedOutput output = new SyndFeedOutput();

            output.output(feed, writer);
            writer.close();

        } catch (HibernateException dbe) {
            dbe.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }

    }

    /**
     * Calculate url.
     *
     * @param newsItem the news item
     * @return the string
     */
    private String calculateURL(NewsItem newsItem) {
        String baseURL = AppConfig.getSiteUrl();
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        String year = yearFormat.format(newsItem.getPublishedDate());
        String month = monthFormat.format(newsItem.getPublishedDate());
        String day = dayFormat.format(newsItem.getPublishedDate());
        String title = newsItem.getTitle().toLowerCase().replace(" ", "-");
        StringBuilder url = new StringBuilder(baseURL);
        url.append("/news/");
        url.append(year);
        url.append("/");
        url.append(month);
        url.append("/");
        url.append(day);
        url.append("/");
        url.append(title);
        return url.toString();
    }
}
