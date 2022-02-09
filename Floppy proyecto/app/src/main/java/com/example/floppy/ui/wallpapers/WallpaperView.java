package com.example.floppy.ui.wallpapers;

import java.util.ArrayList;

public interface WallpaperView {

    void showDefaultWallpapers();
    void showUserWallpapers(ArrayList<String> path);
    void showHandling(Boolean show);
    /**
     * @param  columns cantidad de columnas que va a tener el recycler (2 en portrait y 3 en landscape)
     * */
    void showWallpaperApi(ArrayList<String> body,int columns);
}
