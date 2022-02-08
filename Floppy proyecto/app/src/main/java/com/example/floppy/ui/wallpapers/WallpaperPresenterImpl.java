package com.example.floppy.ui.wallpapers;

import android.app.Activity;
import android.content.Context;

import com.example.floppy.data.Conexion.preferences.Preferences;
import com.example.floppy.data.Conexion.retrofit.RetrofitHelper;
import com.example.floppy.data.Conexion.retrofit.WallpapersApi;
import com.example.floppy.ui.global_presenter.GlobalPresenter;
import com.example.floppy.utils.FilesLocal;
import com.example.floppy.utils.Permission;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;

public class WallpaperPresenterImpl implements WallpaperPresenter {
    private WallpaperView   wallpaperView;
    private Boolean         firstTimer = false;//cargar solamente una vex las imagenes del almacenamiento del movil
    private Preferences     preferences;
    private GlobalPresenter globalPresenter;

    public WallpaperPresenterImpl(Context context, WallpaperView wallpaperView, GlobalPresenter globalPresenter) {
        wallpaperView.showHandling(true);
        this.wallpaperView = wallpaperView;
        wallpaperView.showHandling(false);
        preferences = new Preferences(context);
        this.globalPresenter = globalPresenter;
    }

    @Override
    public void showDefaultWallpapers() {
        wallpaperView.showDefaultWallpapers(); }
    @Override
    public void showUserWallpapers(Context context, Activity activity) {
        globalPresenter.showMessage("Esta función no esta habilitada \nen esta versión");
//        if(Permission.Companion.validatePermissionToGallery(context)){
//            wallpaperView.showHandling(true);
//            new Thread(() -> {
//                if(!firstTimer){
//                    wallpaperView.showUserWallpapers(FilesLocal.Companion.getImages(context));
//                    wallpaperView.showHandling(false);
//                    firstTimer = true;
//                    return ;
//                }
//                wallpaperView.showHandling(false);
//            }).start();
//        }else{
//            Permission.Companion.initValidatePermissionToGallery(activity);
//        }
    }

    @Override
    public void chosenBackground(String background) {
        preferences.saveBackground(background);
        globalPresenter.showMessage("Fondo cambiado");
    }

    @Override
    public void getImagesBackground() {
        wallpaperView.showHandling(true);
        new Thread(() -> {
            WallpapersApi api = RetrofitHelper.INSTANCE.getRetrofit().create(WallpapersApi.class);
            Call<ArrayList<String>> array = api.getImages();
            try {
                wallpaperView.showWallpaperApi(array.execute().body());
                wallpaperView.showHandling(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

//    private static class ImagesGallery {
//        public static ArrayList<Object> listOfImages(Context context) {
//
//
//
//        }
//
//    }
}
