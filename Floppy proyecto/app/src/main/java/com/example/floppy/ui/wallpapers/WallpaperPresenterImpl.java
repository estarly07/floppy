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
    private ArrayList<Object> paths = new ArrayList<>();

    public WallpaperPresenterImpl(WallpaperView wallpaperView) {
        wallpaperView.showHandling(true);
        this.wallpaperView = wallpaperView;
        wallpaperView.showHandling(false);
    }

    @Override
    public void showDefaultWallpapers() { wallpaperView.showDefaultWallpapers(); }

    @Override
    public void showUserWallpapers(Context context, Activity activity) {
        if(Permission.Companion.validatePermissionToGallery(context)){
            wallpaperView.showHandling(true);
            new Thread(() -> {
                FilesLocal.Companion.getAllImages(context,s -> {
                    System.out.println(".showUserWallpapers "+s);
                    wallpaperView.showUserWallpapers(s);
//                    wallpaperView.showHandling(false);
                    return null;
                });

//                paths = paths.size() == 0
//                        ? FilesLocal.Companion.getAllImages(context)
//                        : paths;

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
