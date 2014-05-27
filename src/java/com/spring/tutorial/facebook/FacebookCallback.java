/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.tutorial.facebook;

import com.spring.tutorial.entitites.User;
import com.spring.tutorial.mongo.MongoDB;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Petricioiu
 */
public class FacebookCallback extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws facebook4j.FacebookException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FacebookException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            Facebook facebook = (Facebook) request.getSession().getAttribute("facebook");
            String oauthCode = request.getParameter("code");

            try {

                facebook.getOAuthAccessToken(oauthCode);
                User user = new User();
                user.setEmail("");
                user.setDropboxAccessToken("");
                user.setFacebookToken(oauthCode);
                user.setPassword("");
                user.setUsername(facebook.getName());
                user.setId(facebook.getId());

                MongoDB mongoDB = new MongoDB(user);
                if (mongoDB.registerUser()) {
                    request.getSession().setAttribute("username", user.getUsername());
                    request.getSession().setAttribute("id", user.getId());
                    response.sendRedirect(request.getContextPath() + "/my-drive");
                    
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    facebook.postStatusMessage("I have just registered with File Online Upload :) on " + dateFormat.format(date));
                    
                } else if (!mongoDB.userExist()) {
                    request.getSession().setAttribute("username", user.getUsername());
                    request.getSession().setAttribute("id", user.getId());
                    mongoDB.getUserInfo();
                    request.getSession().setAttribute("dropbox_token", user.getDropboxAccessToken());
                    response.sendRedirect(request.getContextPath() + "/my-drive");
                }

            } catch (FacebookException e) {
                throw new ServletException("mesaj" + e);
            }

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (FacebookException ex) {
            Logger.getLogger(FacebookCallback.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (FacebookException ex) {
            Logger.getLogger(FacebookCallback.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
