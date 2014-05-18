/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spring.tutorial.entitites;

import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author petricioiurobert
 */
public class MongoFile {
    
    private String title;
    private String date;
    private String downloadLink;
    private String type;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    private long size;
    private List<String> tags;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public MongoFile() {
        title = "test";
        date = "12 jun";
        downloadLink = "//";
        type = "text/css";
        size=2;
    }
    public MongoFile(DBObject document) {
        title = (String) document.get("name");
        date = (String) document.get("last_modified");
        downloadLink = document.get("file-id").toString();
        type = (String) document.get("content-type");
        size = (long) document.get("size");
        tags = new ArrayList<>();
        String tagContainer = (String) document.get("tags");
        String[] tagTokens = tagContainer.split(",");
        description = (String) document.get("description");
        tags.addAll(Arrays.asList(tagTokens));
        
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
