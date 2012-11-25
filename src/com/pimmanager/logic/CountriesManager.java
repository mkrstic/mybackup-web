/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pimmanager.logic;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import com.pimmanager.util.DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.Session;

// TODO: Auto-generated Javadoc
/**
 * The Class CountriesManager.
 *
 * @author mladen
 */
@ManagedBean(name = "countriesBean")
@RequestScoped
public class CountriesManager implements Serializable {

    /** The city. */
    private String city;

    /**
     * City complete.
     *
     * @param query the query
     * @return the list
     */
    public List<String> cityComplete(String query) {
        List<String> cities = null;
        cities = getCitiesStartsWith(query.trim());
        return cities;
    }

    /**
     * Gets the cities starts with.
     *
     * @param query the query
     * @return the cities starts with
     * @throws HibernateException the hibernate exception
     */
    public static List<String> getCitiesStartsWith(String query)
            throws HibernateException {
        Connection conn = null;
        PreparedStatement pst = null;
        PreparedStatement pst2 = null;
        Session session = DAO.getSessionFactory().openSession();
        Set<String> cities = new LinkedHashSet<String>();
        try {
            conn = session.connection();
            pst = conn.prepareStatement("SELECT city, countryId FROM cities WHERE city LIKE ? ORDER BY countryId");
            pst2 = conn.prepareStatement("SELECT country FROM countries WHERE countryId=?");
            pst.setString(1, query + "%");
            ResultSet rs = pst.executeQuery();
            Integer lastCountryId = null;
            String lastCountryName = null;
            while (rs.next()) {
                StringBuilder cityName = new StringBuilder("");
                cityName.append(rs.getString("city"));
                int countryId = rs.getInt("countryId");
                if (lastCountryId != null && countryId == lastCountryId) {
                    cityName.append(", ");
                    cityName.append(lastCountryName);
                } else {
                    pst2.setInt(1, countryId);
                    ResultSet rs2 = pst2.executeQuery();
                    if (rs2.next()) {
                        lastCountryId = countryId;
                        lastCountryName = rs2.getString("country");
                        cityName.append(", ");
                        cityName.append(lastCountryName);
                    }
                }
                cities.add(cityName.toString());
            }
            return new ArrayList<String>(cities);
        } catch (SQLException sqle) {
            throw new HibernateException(sqle.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
                if (pst2 != null) {
                    pst2.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
                if (session.isOpen()) {
                    session.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * Gets the city.
     *
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city.
     *
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }
}
