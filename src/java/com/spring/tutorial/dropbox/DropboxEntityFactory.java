/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.tutorial.dropbox;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.spring.tutorial.mongo.MongoData;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Petricioiu
 */
public class DropboxEntityFactory {

    List<DropboxEntity> dropboxFiles;

    public DropboxEntityFactory() {
        dropboxFiles = new ArrayList<>();
    }

    public List<DropboxEntity> getFilesAndFolders(String id, String path) throws UnknownHostException, Exception {
        MongoData mongoData = new MongoData();
        DB db = mongoData.getDB();
        DBCollection collection = db.getCollection(id + "_dropbox_files_meta");
        DBCursor cursor = collection.find();

        while (cursor.hasNext()) {
            DBObject doc = cursor.next();
            String docPath = doc.get("path").toString();
            String docPathHead = docPath.substring(docPath.indexOf("/"), docPath.lastIndexOf("/") + 1);
            
            if (docPathHead.equals(path)) {
                DropboxEntity entity = new DropboxEntity(doc);
                dropboxFiles.add(entity);
            }
        }
        return dropboxFiles;
    }

}
