package com.example.floppy.data.Models;

public class Chat {
    private String idChat;
    private String[] users;
    private Message[] messages;

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }

    public Message[] getMensajes() {
        return messages;
    }

    public void setMensajes(Message[] messages) {
        this.messages = messages;
    }
}
