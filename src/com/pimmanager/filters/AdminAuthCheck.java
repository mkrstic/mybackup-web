package com.pimmanager.filters;

import com.pimmanager.logic.AdminManager;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class AdminAuthCheck.
 */
public class AdminAuthCheck implements Filter {


    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig config) throws ServletException {
    }

     /* (non-Javadoc)
      * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
      */
     public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            AdminManager adminManager = (AdminManager) ((HttpServletRequest) request).getSession(false).getAttribute("adminManager");
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String requestURL = httpRequest.getRequestURL().toString();
            if (requestURL.contains("/admin.login")) {
                if (adminManager == null || (adminManager.getAdmin().isLoggedIn() == false)) {
                    //System.out.println("/admin.login + chaining");
                    chain.doFilter(request, response);
                } else {
                    String homeURL = requestURL.substring(0, requestURL.lastIndexOf("/admin.login")) + "/admin/home";
                    //System.out.println("/signup + redirect");
                    ((HttpServletResponse) response).sendRedirect(homeURL);
                }
            } else {
                if (adminManager == null || (adminManager.getAdmin().isLoggedIn() == false)) {
                    String loginURL = requestURL.substring(0, requestURL.lastIndexOf("/admin"));
                    ((HttpServletResponse) response).sendRedirect(loginURL);
                } else {
                    chain.doFilter(request, response);
                }
            }
        } catch (Exception ex) {
        }
     }

     /* (non-Javadoc)
      * @see javax.servlet.Filter#destroy()
      */
     public void destroy() {
    }
}
