/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.tutorial.formHandlers;

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
public class RegisterController {

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String initForm(ModelMap model) {
        return "account/register";
    }
    
    @ModelAttribute("user")
    public User getUserObject() {
        return new User();
    }
    
    @RequestMapping(value = "/register_user", method = RequestMethod.POST)
    public String addUser(@ModelAttribute("user") User user, ModelMap map, HttpServletRequest request) {
        MongoDB mongo = new MongoDB(user);
        if(mongo.registerUser()) {
            request.getSession().setAttribute("username", user.getUsername());
            request.getSession().setAttribute("id", user.getId());
        }
        return "account/login";
    }
}
