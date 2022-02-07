package com.example.floppy.ui.wallpapers;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.floppy.data.Conexion.preferences.Preferences;
import com.example.floppy.ui.global_presenter.GlobalPresenter;
import com.example.floppy.utils.FilesLocal;
import com.example.floppy.utils.Permission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
        wallpaperView.changeView(true);
        wallpaperView.showDefaultWallpapers(); }
    @Override
    public void showUserWallpapers(Context context, Activity activity) {
        if(Permission.Companion.validatePermissionToGallery(context)){
            wallpaperView.showHandling(true);
            new Thread(() -> {
                if(!firstTimer){
                    wallpaperView.showUserWallpapers(FilesLocal.Companion.getImages(context));
                    wallpaperView.showHandling(false);
                    wallpaperView.changeView(false);
                    firstTimer = true;
                    return ;
                }
                wallpaperView.showHandling(false);
                wallpaperView.changeView(false);
            }).start();
        }else{
            Permission.Companion.initValidatePermissionToGallery(activity);
        }
    }

    @Override
    public void chosenBackground(String background) {
        preferences.saveBackground(background);
        globalPresenter.showMessage("Fondo cambiado");
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
