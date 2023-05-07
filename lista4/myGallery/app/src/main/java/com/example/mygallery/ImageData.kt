package com.example.mygallery

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageData(
    val imageResourceId: Int,
    val description: String,
    var rating: Float
) : Parcelable