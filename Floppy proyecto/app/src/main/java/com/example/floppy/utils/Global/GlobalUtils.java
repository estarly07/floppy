package com.example.floppy.utils.Global;

import android.annotation.SuppressLint;

import java.util.Calendar;

public class GlobalUtils {

    public static String[] COLLECTIONS = {
            "Users",
            "States",
            "Conversations",
            "Stickers"
    };
    @SuppressLint("DefaultLocale")
    public static String getHora() {
        Calendar calendar = Calendar.getInstance();
        int      hour     = calendar.get(Calendar.HOUR_OF_DAY);
        int      minutes  = calendar.get(Calendar.MINUTE);
        return String.format("%02d:%02d", hour, minutes);
    }

    //DATABASE
    public static final String nameDatabase  = "floppy.db";
    public static final String stickersTable = "Stickers" ;
    public static final String userTable     = "user"     ;
    public static final String friendTable   = "friend"   ;



}
