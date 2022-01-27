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
    companion object{
        var mutableList = arrayListOf<String>()
         fun getAllImages(context: Context,callback: (ArrayList<String>) -> Unit){
             GlobalScope.launch(Dispatchers.Main) {
                image(context).collect { callback.invoke(it) }
             }
        }
        private fun image(context:Context)= flow<ArrayList<String>>{
//            if(mutableList.isNotEmpty()){
//                mutableList.forEach {
//                    val imgFile = File(it)
//                    emit(Uri.fromFile(imgFile))
//                    delay(50)
//                }
//                return@flow
//            }


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
            println("Columnas ${cursor.columnCount}")
            while (cursor.moveToNext()) {
                mutableList.add(cursor.getString(columnIndexData))
//                val imgFile = File(cursor.getString(columnIndexData))
//                val uri =Uri.fromFile(imgFile)


//                emit(mutableList)

            }
            emit(mutableList)

        }
    }
}