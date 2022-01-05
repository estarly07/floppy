package com.example.floppy.Domain.Models;

public class Estado {
    String imagen;
    String mensaje;
   // boolean isLike;
    String fecha_inicial;
    String fecha_final;
    int like;
    String nameUser;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
//
//    public boolean isLike() {
//        return isLike;
//    }
//
//    public void setLike(boolean like) {
//        isLike = like;
//    }

    public String getFecha_inicial() {
        return fecha_inicial;
    }

    public void setFecha_inicial(String fecha_inicial) {
        this.fecha_inicial = fecha_inicial;
    }

    public String getFecha_final() {
        return fecha_final;
    }

    public void setFecha_final(String fecha_final) {
        this.fecha_final = fecha_final;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
