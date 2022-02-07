package com.example.floppy.ui.wallpapers;

import android.app.Activity;
import android.content.Context;

public interface WallpaperPresenter {
    void showDefaultWallpapers();
    void showUserWallpapers(Context context, Activity activity);
    void chosenBackground  (String background);
}
