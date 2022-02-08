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
        return  calendar.get(Calendar.YEAR) +"_"+
                calendar.get(Calendar.MONTH)+"_"+
                calendar.get(Calendar.DAY_OF_MONTH)+"_"+
                calendar.get(Calendar.HOUR_OF_DAY)+"_"+
                calendar.get(Calendar.MINUTE)+"_"+
                calendar.get(Calendar.MILLISECOND);
    }
    //Strings
    public static final String packageStickerApk = "com.example.sticker_floppy";
    public static final String nameStickerApk    = "StickerFloppy.apk";
    public static final String urlApk            = "https://github.com/estarly07/floppy/releases/download/v1.0.0/Sticker.apk";
    public static final String baseUrlApi        = "https://stickers-26fd9-default-rtdb.firebaseio.com";

    //DATABASE
    public static final String nameDatabase  = "floppy.db";
    public static final String stickersTable = "Stickers" ;
    public static final String userTable     = "user"     ;
    public static final String friendTable   = "friend"   ;
    public static final String chatTable     = "chat"     ;
    public static final String messageTable  = "mesage"     ;



}
