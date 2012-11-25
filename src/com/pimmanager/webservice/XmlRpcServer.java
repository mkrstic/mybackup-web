/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pimmanager.webservice;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xmlrpc.webserver.XmlRpcServlet;

// TODO: Auto-generated Javadoc
/**
 * The Class XmlRpcServer.
 */
public class XmlRpcServer extends XmlRpcServlet {
   
   /**
    * Instantiates a new xml rpc server.
    */
   public XmlRpcServer() {
       
   }

   /* (non-Javadoc)
    * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      super.doGet(request,response);
   }
   
   /* (non-Javadoc)
    * @see org.apache.xmlrpc.webserver.XmlRpcServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
   @Override
   public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      super.doPost(request,response);
   }

   /* (non-Javadoc)
    * @see org.apache.xmlrpc.webserver.XmlRpcServlet#init(javax.servlet.ServletConfig)
    */
   @Override
   public void init(ServletConfig servletConfig ) throws ServletException {
      super.init(servletConfig);
   }

   /* (non-Javadoc)
    * @see javax.servlet.GenericServlet#destroy()
    */
   @Override
   public void destroy() {
   }


}