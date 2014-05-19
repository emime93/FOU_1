/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spring.tutorial.dropbox;

/**
 *
 * @author Petricioiu
 */
public class DropboxEntity {
    private String name;
    private String size;
    private String lastModified;
    private String path;
    private String rev;

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }
    
    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public String getLastModified() {
        return lastModified;
    }
    public void setType(String type) {
        this.type = type;
    }
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

    @Override
    public String toString() {
        return getName() + ": " + "size: " + getSize() + "type: " + getType() + "path: " + getPath() + 
               "lastModified: " + getLastModified();
    }
    
    
}
