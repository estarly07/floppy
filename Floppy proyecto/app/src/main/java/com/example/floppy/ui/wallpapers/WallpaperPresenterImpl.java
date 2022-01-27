package com.example.floppy.ui.wallpapers;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.floppy.utils.FilesLocal;
import com.example.floppy.utils.Permission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WallpaperPresenterImpl implements WallpaperPresenter {
    private WallpaperView wallpaperView;
    private Boolean       firstTimer = false;//cargar solamente una vex las imagenes del almacenamiento del movil

    public WallpaperPresenterImpl(WallpaperView wallpaperView) {
        wallpaperView.showHandling(true);
        this.wallpaperView = wallpaperView;
        wallpaperView.showHandling(false);
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
                    FilesLocal.Companion.getAllImages(context,s -> {
                        wallpaperView.showUserWallpapers(s);
                        wallpaperView.showHandling(false);
                        wallpaperView.changeView(false);
                        return null;
                    });
                }else{
                    wallpaperView.showHandling(false);
                    wallpaperView.changeView(false);
                }
                firstTimer = true;

            }).start();
        }else{
            Permission.Companion.initValidatePermissionToGallery(activity);
        }

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
