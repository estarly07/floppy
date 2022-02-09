package com.example.floppy.data.Conexion.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    SharedPreferences sharedPreferences;

    final String KEY        = "info"   ;
    final String IDOWNER    = "idOwner";
    final String COLOR      = "color"  ;
    final String BACKGROUND = "BACKGROUND"  ;

    public Preferences(Context context) {
        this.sharedPreferences = context.getSharedPreferences(KEY,Context.MODE_PRIVATE);
    }

    /**
     * GUARDAR EL ID DEL USUARIO QUE SE LOGUEO O REGISTRO
     *
     * @param idOwner id del usuario logueado o registrado
     */
    public void saveIdOwner(String idOwner) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(IDOWNER, idOwner );
        editor.apply();
    }
    /**COLOR BACKGROUND ESCOGIDO POR EL USER*/
    public void chooseColor(int color){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(COLOR,color);
        editor.apply();
    }
    /**COLOR BACKGROUND ESCOGIDO POR EL USER*/
    public void saveBackground(String background){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BACKGROUND,background);
        editor.apply();
    }

    public String getIdOwner     (){ return sharedPreferences.getString(IDOWNER   ,""); }
    public int getColorBackground(){ return sharedPreferences.getInt   (COLOR     ,  0); }
    public String getBackground  (){ return sharedPreferences.getString(BACKGROUND,""); }
}
