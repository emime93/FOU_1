/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.tutorial.dropbox;

import com.dropbox.core.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Petricioiu
 */
public class DropBoxAuth {

    DbxClient client;
    
    public DropBoxAuth() {
    }

    public void authenticate(String token) throws IOException, DbxException {
        final String APP_KEY = "mt6yctmupgrodlm";
        final String APP_SECRET = "p68kiiyvdctftuv";

        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

        DbxRequestConfig config = new DbxRequestConfig(
                "JavaTutorial/1.0", Locale.getDefault().toString());
        client = new DbxClient(config, token);
       // "dTtFItzZ4mMAAAAAAAAJQN1aaY_VVlS6S1ejL4vK7s3fKJFi9zplHsD_Ium7w1g_"
       
    }
    
    public List<DropboxEntity> getFilesAndFolders(DbxClient client, String path) throws DbxException {
        List<DropboxEntity> entityList = new ArrayList<>();
        
        DbxEntry.WithChildren listing = client.getMetadataWithChildren(path);
        System.out.println("Files in the root path:");
        for (DbxEntry child : listing.children) {
            String fileInfo = child.toString();
            DropboxEntity entity;
            if (child.isFile()) {
                String rev = fileInfo.substring(fileInfo.indexOf("rev=") + 5,fileInfo.indexOf(")") - 1);
                String fileSize = fileInfo.substring(fileInfo.indexOf("humanSize=") + 11,fileInfo.indexOf("\", lastModified"));
                String fileLastModified = fileInfo.substring(fileInfo.indexOf("lastModified=") + 14,fileInfo.indexOf("\", clientMtime")); 
                entity = new DropboxEntity(child.name, "file", fileSize, fileLastModified, child.path, rev);
            }
            else {
                entity = new DropboxEntity(child.name, "folder", child.path);
            }
            entityList.add(entity);
        }
        return entityList;
    }
}
