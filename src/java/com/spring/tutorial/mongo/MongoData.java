/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spring.tutorial.mongo;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;

/**
 *
 * @author petricioiurobert
 */
public class MongoData {

    private MongoClient mongoClient;
    private DB db;
    private boolean auth = false;
    
    public MongoData() throws UnknownHostException {
        mongoClient = new MongoClient();
        db = mongoClient.getDB("fou");
        char[] pass = "mongo".toCharArray();
        auth = db.authenticate("emime", pass);
    }
    public DB getDB() throws Exception {
        if (auth == true)
            return db;
        throw new Exception("Something went wrong with the connection");
    }

    @Override
    protected void finalize() throws Throwable {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
    
    
}
