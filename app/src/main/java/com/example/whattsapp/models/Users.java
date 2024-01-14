package com.example.whattsapp.models;

public class Users {
    String userName, userId;
    String mail, password;
    String profilePic, lastMessage;

    public Users() {
        //empty constructor
    }

    public Users(String userName, String mail, String password) {
        //sign up constructor
        this.userName = userName;
        this.mail = mail;
        this.password = password;
    }

    public Users(String userName, String userId, String mail, String password, String profilePic, String lastMessage) {
        //default constructor
        this.userName = userName;
        this.userId = userId;
        this.mail = mail;
        this.password = password;
        this.profilePic = profilePic;
        this.lastMessage = lastMessage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId(String key) {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
