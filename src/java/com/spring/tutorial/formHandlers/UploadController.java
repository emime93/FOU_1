/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.tutorial.formHandlers;

import com.spring.tutorial.entitites.FileUploader;
import java.io.IOException;
import java.io.InputStream;
import javax.mail.Multipart;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author petricioiurobert
 */
@Controller
public class UploadController {

    @RequestMapping(value = "/my-drive/upload", method = RequestMethod.GET)
    public String upload(ModelMap map) {
        map.addAttribute("title", "upload");
        return "mydrive/upload";
    }
    
    @RequestMapping(value = "/my-drive/fileupload", method = RequestMethod.POST) 
    public @ResponseBody String dropboxUpload(ModelMap map) {
        String boom = "boom";
        int x = 2;
        return "";
    }
    
}
