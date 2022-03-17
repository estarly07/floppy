package com.example.floppy.utils;

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.example.floppy.utils.Permission.Companion.initValidatePermissionToRecord

class Permission {
     companion object{
         const val RECORD_CODE_PERMISSION  = 1
         const val GALLERY_CODE_PERMISSION = 2
         const val WRITE_CODE_PERMISSION   = 5

         const val CODE_GALLERY            = 3
         const val REQUEST_IMAGE_CAPTURE   = 4;

         fun Context.validatePermissionToRecord():Boolean
                 =  ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO          ) == PackageManager.PERMISSION_GRANTED
                 && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                 && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED

         fun Activity.initValidatePermissionToRecord(){
             ActivityCompat.requestPermissions(
                 this,
                 listOf(
                     Manifest.permission.RECORD_AUDIO,
                     Manifest.permission.READ_EXTERNAL_STORAGE,
                     Manifest.permission.WRITE_EXTERNAL_STORAGE).toTypedArray(),
                 RECORD_CODE_PERMISSION)
         }

         fun Context.validatePermissionToGallery():Boolean
                 =  ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE  ) == PackageManager.PERMISSION_GRANTED

         fun Context.validatePermissionToWrite():Boolean
                 =  ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE  ) == PackageManager.PERMISSION_GRANTED
         fun Activity.initValidatePermissionToWrite(){
             ActivityCompat.requestPermissions(
                 this,
                 listOf(
                     Manifest.permission.WRITE_EXTERNAL_STORAGE).toTypedArray(),
                 RECORD_CODE_PERMISSION)
         }

         fun Activity.initValidatePermissionToGallery(){
             ActivityCompat.requestPermissions(
                 this,
                 listOf(
                     Manifest.permission.READ_EXTERNAL_STORAGE).toTypedArray(),
                 GALLERY_CODE_PERMISSION)
         }

         fun IntArray.validateResultsPermissions(): Boolean = this.contains(PackageManager.PERMISSION_DENIED) && this.isNotEmpty()

     }
}
