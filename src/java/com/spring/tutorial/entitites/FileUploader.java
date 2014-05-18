/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.tutorial.entitites;

/**
 *
 * @author petricioiurobert
 */
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import com.spring.tutorial.mongo.MongoDB;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.springframework.http.HttpRequest;
import sun.misc.IOUtils;

public class FileUploader {

    private static HttpServletRequest request;
    private static HttpServletResponse response;

    public FileUploader(HttpServletRequest req, HttpServletResponse res) {
        this.request = req;
        this.response = res;
    }

    public String upload() throws IOException, ServletException, FacebookException {
        OutputStream output = null;
        InputStream fileContent = null;
        final Part filePart;
        
        final File file;
        try {
            filePart = request.getPart("file");
           
            fileContent = filePart.getInputStream();

            MongoClient mongoClient = new MongoClient();
            mongoClient = new MongoClient();
            DB db = mongoClient.getDB("fou");

            char[] pass = "mongo".toCharArray();
            boolean auth = db.authenticate("admin", pass);

            file = File.createTempFile("fileToStore", "tmp");

            file.deleteOnExit();
            FileOutputStream fout = new FileOutputStream(file);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = fileContent.read(bytes)) != -1) {
                fout.write(bytes, 0, read);
            }

            GridFS gridFS = new GridFS(db, request.getSession().getAttribute("username") + "_files");
            GridFSInputFile gfsInputFile = gridFS.createFile(file);
            gfsInputFile.setFilename(filePart.getSubmittedFileName());
            gfsInputFile.save();

            DBCollection collection = db.getCollection(request.getSession().getAttribute("username") + "_files_meta");
            BasicDBObject metaDocument = new BasicDBObject();
            metaDocument.append("name", filePart.getSubmittedFileName());
            metaDocument.append("size", filePart.getSize());
            metaDocument.append("content-type", filePart.getContentType());
            metaDocument.append("file-id", gfsInputFile.getId());
            metaDocument.append("tags", request.getParameter("tags"));
            metaDocument.append("description", request.getParameter("description"));

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            metaDocument.append("last_modified", dateFormat.format(new Date()));

            collection.insert(metaDocument);

        } catch (Exception e) {
            return "message:" + e.getMessage();
        } finally {
            if (output != null) {
                output.close();
            }
            if (fileContent != null) {
                fileContent.close();
            }
        }

        return "success";
    }
}
