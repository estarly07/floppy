package com.example.floppy.data.Services;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.core.content.FileProvider;

import com.example.floppy.BuildConfig;
import com.example.floppy.ui.message.MessagePresenter;

import java.io.File;

public class ServiceDownload {
    private static long downloadId;
    private MessagePresenter presenter;

    public ServiceDownload(MessagePresenter presenter) {
        this.presenter = presenter;
    }

    public void beginDownload(Context context) {
        File file = new File(context.getExternalFilesDir(null), "StickerFloppy.apk");
        DownloadManager.Request request;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            request = new DownloadManager.Request(Uri.parse("https://files.an1.net/nova-launcher-prime_7.0.57-an1.com.apk"))
                    .setTitle("Sticker Floppy")
                    .setDescription("Descargando Sticker Floppy")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationUri(Uri.fromFile(file))
                    .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true);
        } else {
            request = new DownloadManager.Request(Uri.parse("https://files.an1.net/nova-launcher-prime_7.0.57-an1.com.apk"))
                    .setTitle("Sticker Floppy")
                    .setDescription("Descargando Sticker Floppy")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationUri(Uri.fromFile(file))
                    .setAllowedOverRoaming(true);
        }
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadId = downloadManager.enqueue(request);
    }

    public BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadId == id) { presenter.installApk(); }
        }
    };

}
