/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spring.tutorial.formHandlers;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.spring.tutorial.entitites.LoggedUser;
import com.spring.tutorial.entitites.User;
import com.spring.tutorial.mongo.MongoDB;
import java.net.BindException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.SimpleFormController;

/**
 *
 * @author petricioiurobert
 */
@Controller
public class LoginController {
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String initForm(ModelMap model) {
        return "account/login";
    }

    @ModelAttribute("loginUser")
    public LoggedUser getLoggedUser(){
        return new LoggedUser();
    }
    
    @RequestMapping(value = "/login_user", method = RequestMethod.POST)
    public String loginUser(@ModelAttribute("loginUser") LoggedUser user, ModelMap map, HttpServletRequest request) {
        User _user = new User();
        _user.setEmail("");
        _user.setPassword(user.getPassword());
        _user.setUsername(user.getUsername());
        
        MongoDB mongo = new MongoDB(_user);
        if (mongo.checkUser()) { 
            request.getSession().setAttribute("username", user.getUsername());
            DBCollection collection = mongo.getDb().getCollection("users");
            DBCursor cursor = collection.find();
            while (cursor.hasNext()) {
                DBObject document = cursor.next();
                if (document.get("username").equals(user.getUsername()) &&
                    document.get("password").equals(user.getPassword())) {
                    request.getSession().setAttribute("dropbox_token", document.get("dropbox_token"));
                }
            }
            return "redirect:my-drive";
        }
        return "redirect:signup";
    }
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        request.getSession().setAttribute("username", null);
        return "redirect:/login";
    }
}
