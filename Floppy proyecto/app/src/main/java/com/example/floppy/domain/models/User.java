package com.example.floppy.domain.models;

import com.example.floppy.domain.entities.FriendEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String      idUser;
    private String      name;
    private String      pass;
    private String      email;
    private String      photo;
    private String      messageUser;//about user
    private Estado_User estado_user;
    private ArrayList<FriendEntity> friendEntities = new ArrayList<>();

    private User() { }

    public User(String idUser, String name, String pass, String email, String photo, String messageUser, Estado_User estado_user) {
        this.idUser = idUser;
        this.name   = name;
        this.pass   = pass;
        this.email  = email;
        this.photo  = photo;
        this.messageUser = messageUser;
        this.estado_user = estado_user;
    }

    private static User instance = null;

    public static User getInstance() {
        if(instance!=null){ return instance; }
        instance = new User();
        return instance;
    }

    public Map<String,Object> toMap(User user){
        return new HashMap<String,Object>(){{
            put("idUser"     ,user.getIdUser());
            put("name"       ,user.getName());
            put("pass"       ,user.getPass());
            put("email"      ,user.getEmail());
            put("photo"      ,user.getPhoto());
            put("messageUser",user.getMessageUser());
            put("estado_user",user.getEstado_user()); }
        };
    }

    public String getPass() { return pass; }

    public void setPass(String pass) { this.pass = pass; }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public ArrayList<FriendEntity>  getFriends() {
        return friendEntities;
    }

    public void setFriends(FriendEntity friendEntity) {
        this.friendEntities.add(friendEntity);
    }

    public Estado_User getEstado_user() {
        return estado_user;
    }

    public void setEstado_user(Estado_User estado_user) {
        this.estado_user = estado_user;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) { this.email = email; }
}
