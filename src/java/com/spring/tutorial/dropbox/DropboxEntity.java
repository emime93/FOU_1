/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spring.tutorial.dropbox;

import com.mongodb.DBObject;
import com.spring.tutorial.entitites.FOUFile;

/**
 *
 * @author Petricioiu
 */
public class DropboxEntity extends FOUFile{
    
    private String name;
    private String size;
    private String lastModified;
    private String path;
    private String rev;

    @Override
    public String getRev() {
        return rev;
    }

    @Override
    public void setRev(String rev) {
        this.rev = rev;
    }
    
    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSize() {
        return size;
    }

    @Override
    public String getLastModified() {
        return lastModified;
    }
    @Override
    public void setType(String type) {
        this.type = type;
    }
    @Override
    public String getType() {
        return type;
    }
    private String type;

    public DropboxEntity(String name, String type, String path) {
        this.name = name;
        this.type = type;
        this.path = path;
    }
    public DropboxEntity(String name, String type, String size, String lastModified, String path, String rev) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.lastModified = lastModified;
        this.path = path;
        this.rev = rev;
    }
    public DropboxEntity (DBObject doc) {
        this.name = (String) doc.get("name");
        this.type = (String) doc.get("type");
        this.size = (String) doc.get("fileSize");
        this.lastModified = (String) doc.get("lastModified");
        this.path = (String) doc.get("path");
        this.rev = (String) doc.get("rev");
    }

    @Override
    public String toString() {
        return getName() + ": " + "size: " + getSize() + "type: " + getType() + "path: " + getPath() + 
               "lastModified: " + getLastModified();
    }
    
    
}
