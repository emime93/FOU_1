/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.tutorial.entitites;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxWriteMode;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import facebook4j.FacebookException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author Petricioiu
 */
public class DropboxUploader {

    private static HttpServletRequest request;
    private static HttpServletResponse response;
    private DbxClient client;
    public DropboxUploader(HttpServletRequest req, HttpServletResponse res,DbxClient client) {
        this.request = req;
        this.response = res;
        this.client = client;
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

            FileInputStream inputStream = new FileInputStream(file);
            try {
                String fileName = filePart.getSubmittedFileName();
                String path = request.getParameter("path");
                DbxEntry.File uploadedFile = client.uploadFile(path + "/" + filePart.getSubmittedFileName(),
                        DbxWriteMode.add(), file.length(), inputStream);
                System.out.println("Uploaded: " + uploadedFile.toString());
            } finally {
                inputStream.close();
            }

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
