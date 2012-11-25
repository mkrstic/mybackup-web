package com.pimmanager.filters;

import com.pimmanager.configuration.AppConfig;
import com.pimmanager.logic.UserManager;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserAuthCheck implements Filter {

    
    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig config) throws ServletException {
    }

     public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            UserManager userManager = (UserManager) ((HttpServletRequest) request).getSession(false).getAttribute("userBean");
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String requestURL = httpRequest.getRequestURL().toString();
            if (requestURL.contains("/signup")) {
                if (userManager == null || (userManager.getUser().isLoggedIn() == false)) {
                    //System.out.println("/signup + chaining");
                    chain.doFilter(request, response);
                } else {
                    String homeURL = AppConfig.getSiteUrl();
                    //System.out.println("/signup + Redirektovanje");
                    ((HttpServletResponse) response).sendRedirect(homeURL);
                }
            } else {
                if (userManager == null || (userManager.getUser() == null) || (userManager.getUser().isLoggedIn() != true)) {
                    String homeURL = AppConfig.getSiteUrl();
                    ((HttpServletResponse) response).sendRedirect(homeURL);
                    //System.out.println("/profile + Redirektovanje");
                } else {
                    //System.out.println("/profile + chaining");
                    chain.doFilter(request, response);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
     }
     
     public void destroy() {
    }
}
