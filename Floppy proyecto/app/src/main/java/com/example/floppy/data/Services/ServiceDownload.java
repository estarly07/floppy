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

import java.io.File;

public class ServiceDownload {
    private static long downloadId;

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
            request = new DownloadManager.Request(Uri.parse(""))
                    .setTitle("https://github.com/estarly07/My-Books/releases/download/v1.2.0/My.Books.apk")
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
            if (downloadId == id) {

                File file = new File(context.getExternalFilesDir(null), "StickerFloppy.apk");
                Intent intentApk ;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri path = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
                    intentApk = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                    intentApk.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentApk.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intentApk.setData(path);
                }else{
                    Uri apkUri = Uri.fromFile(file);
                    intentApk = new Intent(Intent.ACTION_VIEW);
                    intentApk.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    intentApk.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                try {
                    context.startActivity(intentApk);
                } catch (ActivityNotFoundException e) {
                }
            }
        }
    };

}
