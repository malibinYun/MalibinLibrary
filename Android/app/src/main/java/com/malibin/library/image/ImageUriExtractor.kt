package com.malibin.library.image

import android.content.Intent

/**
 * @author Malibin
 *
 * Created on 2020 07 07
 * Updated on 2020 07 07
 *
 * @sample
 * val imageUris = ImageUriExtractor.from(intent)
 * or
 * val imageUris = intent.getImageUris()
 *
 * @see ImageUri
 *
 * intent를 넣기만 하면 알아서 imageUri의 리스트를 뱉어주는 Util class 입니다.
 * onActivityResult를 통해 이미지가 들어온 경우에만 사용 가능합니다.
 *
 */

object ImageUriExtractor {
    fun from(intent: Intent?): List<ImageUri> {
        if (intent == null) return emptyList()
        if (intent.hasSingleImage()) return getSingleImage(intent)
        return getMultipleImages(intent)
    }

    private fun Intent.hasSingleImage(): Boolean {
        return this.data != null
    }

    private fun getSingleImage(intent: Intent): List<ImageUri> {
        val imageUri = intent.data
            ?: throw IllegalArgumentException("getSingleImage should not be called when multiple images loaded")
        return listOf(ImageUri(imageUri))
    }

    private fun getMultipleImages(intent: Intent): List<ImageUri> {
        val clipData = intent.clipData
            ?: throw IllegalArgumentException("getMultipleImages should not be called when single image loaded")

        return IntRange(0, clipData.itemCount - 1)
            .map { clipData.getItemAt(it).uri }
            .map { ImageUri(it) }
    }
}

fun Intent.getImageUris(): List<ImageUri> = ImageUriExtractor.from(this)

