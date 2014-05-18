/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spring.tutorial.entitites;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ReadPreference;
import com.spring.tutorial.mongo.MongoData;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author petricioiurobert
 */
public class MongoFileManager {
    
    
    
    private DBCollection collection;
    private List<MongoFile> files;
    
    public MongoFileManager(String user) throws UnknownHostException {
        MongoData data = new MongoData();
        try {
            collection = data.getDB().getCollection(user + "_files_meta");
            files = new ArrayList<>();
            DBCursor cursor = collection.find();
            
            while (cursor.hasNext()) {                
                DBObject document = cursor.next();
                MongoFile file = new MongoFile(document);
                files.add(file);
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public List<MongoFile> getFiles() {
        return files;
    }
    
}
