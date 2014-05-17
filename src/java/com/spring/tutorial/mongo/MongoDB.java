/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.tutorial.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.spring.tutorial.entitites.User;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author petricioiurobert
 */
public class MongoDB {

    private MongoClient mongoClient;
    private static User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    private static boolean auth;
    private DB db;

    public DB getDb() {
        return db;
    }

    public MongoDB(User user) {
        try {
            setUser(user);
            mongoClient = new MongoClient();
            db = mongoClient.getDB("fou");

            char[] pass = "mongo".toCharArray();
            auth = db.authenticate("emime", pass);

        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
    }
    public void getUserInfo() {
        if (auth) {
            DBCollection collection = db.getCollection("users");
            DBCursor cursor = collection.find();
            while (cursor.hasNext()) {
                DBObject document = cursor.next();
                
                if (document.get("username").equals(user.getUsername())) {
                    user.setDropboxAccessToken((String) document.get("dropbox_token"));
                }
            }
        }
    }
    public boolean userExist() {
        //return true if user doesn't exist
        if (auth) {
            DBCollection collection = db.getCollection("users");
            DBCursor cursor = collection.find();
            while (cursor.hasNext()) {
                if (cursor.next().get("username").equals(user.getUsername())) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean registerUser() {
        if (userExist()) {
            try {

                BasicDBObject document = new BasicDBObject();
                document.append("username", user.getUsername());
                document.append("email", user.getEmail());
                document.append("password", user.getPassword());
                document.append("dropbox_token", "");
                document.append("facebook_token", user.getFacebookToken());

                DBCollection collection = db.getCollection("users");
                collection.insert(document);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public boolean checkUser() {
        if (auth) {
            DBCollection collection = db.getCollection("users");
            DBCursor cursor = collection.find();
            while (cursor.hasNext()) {
                DBObject document = cursor.next();
                if (document.get("username").equals(user.getUsername())
                        && document.get("password").equals(user.getPassword())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void finalize() throws Throwable {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

}
