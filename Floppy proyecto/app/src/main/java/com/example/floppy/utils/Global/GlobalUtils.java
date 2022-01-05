package com.example.floppy.utils.Global;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class GlobalUtils {

    public static String[] COLLECTIONS = {
            "Users",
            "States",
            "Conversations",
            "Stickers"
    };
    @SuppressLint("DefaultLocale")
    public static String getHour() {
        Calendar calendar = Calendar.getInstance();
        int      hour     = calendar.get(Calendar.HOUR_OF_DAY);
        int      minutes  = calendar.get(Calendar.MINUTE);
        return String.format("%02d:%02d", hour, minutes);
    }
    @SuppressLint("DefaultLocale")
    public static String getDateNow() {
        Calendar calendar = Calendar.getInstance();
        return  calendar.get(Calendar.YEAR) +"/"+
                calendar.get(Calendar.MONTH)+"/"+
                calendar.get(Calendar.DAY_OF_MONTH);
    }
    //Strings
    public static final String packageStickerApk = "com.example.sticker_floppy";
    public static final String nameStickerApk    = "StickerFloppy.apk";

    //DATABASE
    public static final String nameDatabase  = "floppy.db";
    public static final String stickersTable = "Stickers" ;
    public static final String userTable     = "user"     ;
    public static final String friendTable   = "friend"   ;



}
