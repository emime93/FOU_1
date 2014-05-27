/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spring.tutorial.entitites;

/**
 *
 * @author petricioiurobert
 */
public class User {
    private String username;
    private String dropboxAccessToken;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDropboxAccessToken() {
        return dropboxAccessToken;
    }

    public void setDropboxAccessToken(String dropboxAccessToken) {
        this.dropboxAccessToken = dropboxAccessToken;
    }

    public String getFacebookToken() {
        return facebookToken;
    }

    public void setFacebookToken(String facebookToken) {
        this.facebookToken = facebookToken;
    }
    private String facebookToken;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    private String password;
    private String email;

}
