package com.spring.tutorial.controllers;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.spring.tutorial.entitites.MongoFile;
import com.spring.tutorial.entitites.MongoFileManager;
import com.spring.tutorial.mongo.MongoData;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.portlet.ModelAndView;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author petricioiurobert
 */
@Controller
public class DefaultController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(ModelMap map) {
        map.addAttribute("title", "index");
        return "index";
    }
    @RequestMapping(value = "/my-drive", method = RequestMethod.GET) 
    public String myDrive(ModelMap map, HttpServletRequest request){
        map.addAttribute("title","my-drive");
        try {
            
            MongoFileManager fileManager = new MongoFileManager(request.getSession().getAttribute("username").toString());
            map.addAttribute("files",fileManager.getFiles());
            
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
        
        return "mydrive/mydrive";
    }
    @RequestMapping(value = "/files/{id}", method = RequestMethod.GET) 
    public String getFile(@PathVariable("id") String id, ModelMap map,
                            HttpServletRequest request, HttpServletResponse response) throws IOException, Exception{
        
        
        String username = request.getSession().getAttribute("username").toString();
        MongoData data = new MongoData();
        GridFS collection = new GridFS(data.getDB(),username + "_files");
        
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        GridFSDBFile file = collection.findOne(query);
        
        DBCollection metaFileCollection = data.getDB().getCollection(username + "_files_meta");
        BasicDBObject metaQuery = new BasicDBObject();
        metaQuery.put("file-id",new ObjectId(id));
        DBObject metaFileDoc = metaFileCollection.findOne(metaQuery);
        MongoFile metaFile = new MongoFile(metaFileDoc);
        
        ServletOutputStream out = response.getOutputStream();
        String mimeType = metaFile.getType();
        response.setContentType(mimeType);
        response.setContentLength((int) file.getLength());
        String headerKey = "Content-Disposition";
        
        File f = File.createTempFile(file.getFilename(), metaFile.getType().substring(metaFile.getType().indexOf("/")+1));
        String headerValue = String.format("attachment; filename=\"%s\"",
                file.getFilename());
        response.setHeader(headerKey, headerValue);
        file.writeTo(f);
        
        FileInputStream inputStream = new FileInputStream(f);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        
        while (inputStream.read(buffer, 0, 4096) != -1) 
            out.write(buffer,0,4096);
        inputStream.close();
        out.flush();
        out.close();
        
        return "mydrive/temp";
    }
    

}
