package com.example.floppy.data.Conexion.retrofit

import com.example.floppy.utils.global.GlobalUtils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    fun getRetrofit():Retrofit {
        return Retrofit.Builder()
            .baseUrl(GlobalUtils.baseUrlApi)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}