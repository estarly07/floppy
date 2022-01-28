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
import java.io.File

class FilesLocal {
    companion object {
        var mutableList = arrayListOf<String>()
        fun getImages(context: Context): ArrayList<String> {
            val cursor: Cursor
            val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection =
                arrayOf( MediaStore.Images.Thumbnails.DATA )
//                arrayOf(MediaStore.Images.ImageColumns.DATA,MediaStore.MediaColumns.DATA,
//                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Media.BUCKET_ID)
            val orderBy = MediaStore.Video.Media.DATE_TAKEN
            cursor = context.contentResolver.query(
                uri, projection, null, null,
                "$orderBy DESC"
            )!!
            val columnIndexData: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            println("Columnas ${cursor.columnCount}")
            while (cursor.moveToNext()) {
                mutableList.add(cursor.getString(columnIndexData))
//                val imgFile = File(cursor.getString(columnIndexData))
//                val uri =Uri.fromFile(imgFile)


//                emit(mutableList)

            }
            return mutableList

        }
    }

}