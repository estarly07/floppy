package com.example.floppy.ui.wallpapers;

import java.util.ArrayList;

public interface WallpaperView {

    void showDefaultWallpapers();
    void showUserWallpapers(ArrayList<String> path);
    void changeView(Boolean show);
    void showHandling(Boolean show);
}
