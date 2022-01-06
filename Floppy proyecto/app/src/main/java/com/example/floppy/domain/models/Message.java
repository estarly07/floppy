package com.example.floppy.domain.models;

public class Message {

    public enum TypesMessages{TEXT,STICKER}

    private String idMessage;
    private String message;
    private String user;/**USUARIO QUE LO ENVIO*/
    private String date;
    private String hora;
    private TypesMessages typeMessage;
    private EstadosMensajes state;

    public String getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(String idMessage) {
        this.idMessage = idMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String  user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public EstadosMensajes getState() {
        return state;
    }

    public void setState(EstadosMensajes state) {
        this.state = state;
    }

    public TypesMessages getTypeMessage() { return typeMessage; }

    public void setTypeMessage(TypesMessages typeMessage) { this.typeMessage = typeMessage; }
}
