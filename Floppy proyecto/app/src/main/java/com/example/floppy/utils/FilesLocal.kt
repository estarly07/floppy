package com.example.floppy.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.ArrayList

class FilesLocal {
    companion object{
         fun getAllImages(context: Context,callback: (String) -> Unit){
             GlobalScope.launch(Dispatchers.IO) {
                image(context).collect { callback.invoke(it) }
             }
        }
        private fun image(context:Context)= flow<String>{
            val cursor: Cursor
            val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection =
                arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

            val orderBy = MediaStore.Video.Media.DATE_TAKEN
            cursor = context.contentResolver.query(
                uri, projection, null, null,
                "$orderBy DESC"
            )!!
            val columnIndexData: Int = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            while (cursor.moveToNext()) {
               emit(cursor.getString(columnIndexData))
                delay(500)


//                val imgFile = File(absolutePathOfImage)
//                                Uri imageUri = Uri.fromFile(imgFile);
//
//                try {
//
//                    BitmapFactory.decodeFile(String.valueOf(MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri)));
//                    Bitmap resizedBitmap =
//                            BitmapFactory.decodeFile(String.valueOf(MediaStore.Images.Media.getBitmap(context.getContentResolver(),
//                                    imageUri)) );
//                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getPath());
//                   Bitmap resizedBitmap= Bitmap.createScaledBitmap(bitmap, 100 , 100, false);
//
//
//                    listOfAllImages.add(resizedBitmap);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }

        }
    }
}