package com.example.floppy.ui.wallpapers;

import android.net.Uri;

public interface WallpaperView {

    void showDefaultWallpapers();
    void showUserWallpapers(Uri path);
    void changeView(Boolean show);
    void showHandling(Boolean show);
}
