package com.example.floppy.data.Services;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import java.io.File;

public class ServiceDownload {
    private long downloadId;
    public void beginDownload(Context context) {
        File file = new File(context.getExternalFilesDir(null), "StickerFloppy");
        DownloadManager.Request request;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            request = new DownloadManager.Request(Uri.parse("https://github.com/estarly07/My-Books/releases/download/v1.2.0/My.Books.apk"))
                    .setTitle("Floppy")
                    .setDescription("Descargando Sticker Floppy")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationUri(Uri.fromFile(file))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true);
        } else {
            request = new DownloadManager.Request(Uri.parse(""))
                    .setTitle("Floppy")
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
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
            if(downloadId == id){
                System.out.println("Listo mi pana");
            }
        }
    };

}
