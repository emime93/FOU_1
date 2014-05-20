/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.tutorial.dropbox;

import com.dropbox.core.*;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.spring.tutorial.mongo.MongoData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
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

    public void fetchFilesAndFolders(String user, DbxClient client, String path) throws DbxException, UnknownHostException, Exception {
      
        MongoData mongoData = new MongoData();
        DB db = mongoData.getDB();
        DBCollection collection = db.getCollection(user + "_dropbox_files_meta");

        DbxEntry.WithChildren listing = client.getMetadataWithChildren(path);

        for (DbxEntry child : listing.children) {
            String fileInfo = child.toString();
            DropboxEntity entity;
            if (child.isFile()) {
                String rev = fileInfo.substring(fileInfo.indexOf("rev=") + 5, fileInfo.indexOf(")") - 1);
                String fileSize = fileInfo.substring(fileInfo.indexOf("humanSize=") + 11, fileInfo.indexOf("\", lastModified"));
                String fileLastModified = fileInfo.substring(fileInfo.indexOf("lastModified=") + 14, fileInfo.indexOf("\", clientMtime"));

                entity = new DropboxEntity(child.name, "file", fileSize, fileLastModified, child.path, rev);

                DBObject document = new BasicDBObject();
                document.put("rev", rev);
                document.put("fileSize", fileSize);
                document.put("lastModified", fileLastModified);
                document.put("name", child.name);
                document.put("path", child.path);
                document.put("type", "file");

                DBCursor cursor = collection.find();
                boolean found = false;

                while (cursor.hasNext()) {
                    DBObject doc = cursor.next();
                    if (doc.get("path").equals(child.path)) {
                        found = true;
                    }
                }

                if (found) {
                    document.put("found", "true");
                    BasicDBObject newDocument = new BasicDBObject();
                    newDocument.append("$set", document);
                    BasicDBObject searchQuery = new BasicDBObject().append("path", child.path);
                    collection.update(searchQuery, newDocument);
                    BasicDBObject documentToUpdate = new BasicDBObject();
                    documentToUpdate.put("found", "false");
                    collection.remove(documentToUpdate);
                }
                else {
                    document.put("found", "false");
                    collection.insert(document);
                }
                
            } else {
                entity = new DropboxEntity(child.name, "folder", child.path);
                
                DBObject document = new BasicDBObject();
                document.put("rev", "");
                document.put("fileSize", "");
                document.put("lastModified", "");
                document.put("name", child.name);
                document.put("path", child.path);
                document.put("type", "folder");
                
                DBCursor cursor = collection.find();
                boolean found = false;
                if (cursor.hasNext())
                while (cursor.hasNext()) {
                    DBObject doc = cursor.next();
                    if (doc.get("path").equals(child.path)) {
                        found = true;
                    }
                }

                if (found) {
                    document.put("found", "true");
                    BasicDBObject newDocument = new BasicDBObject();
                    newDocument.append("$set", document);
                    BasicDBObject searchQuery = new BasicDBObject().append("path", child.path);
                    collection.update(searchQuery, newDocument);
                    BasicDBObject documentToUpdate = new BasicDBObject();
                    documentToUpdate.put("found", "false");
                    collection.remove(documentToUpdate);
                }
                else {
                    document.put("found", "false");
                    collection.insert(document);
                }
                fetchFilesAndFolders(user, client, child.path);
            }
        }
    }
}
