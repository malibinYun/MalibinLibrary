package com.malibin.library.image

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore

/**
 * @author Malibin
 *
 * Created on 2020 07 07
 * Updated on 2020 07 07
 *
 * Uri를 wrapping한 객체입니다.
 *
 * @see getRealPath
 * 이 메서드를 통해 Uri의 절대 경로를 가져올 수 있습니다.
 *
 * @see ImageUriExtractor
 * @see getImageUris
 *
 */

data class ImageUri(
    val uri: Uri,
) {
    fun getRealPath(context: Context): String {
        var filePath = ""
        val wholeID = DocumentsContract.getDocumentId(uri)

        val id = wholeID.split(":").toTypedArray()[1]
        val column = arrayOf(MediaStore.Images.Media.DATA)

        val sel = MediaStore.Images.Media._ID + "=?"
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            column, sel, arrayOf(id), null
        ) ?: return ""

        val columnIndex: Int = cursor.getColumnIndex(column[0])
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex)
        }
        cursor.close()
        return filePath
    }
}
