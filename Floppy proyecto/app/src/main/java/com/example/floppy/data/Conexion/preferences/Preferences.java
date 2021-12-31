package com.example.floppy.data.Conexion.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    SharedPreferences sharedPreferences;

    final String KEY    = "info" ;
    final String IDOWNER  = "idOwner";

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

    public String getIdOwner(){ return sharedPreferences.getString(IDOWNER,""); }
}
