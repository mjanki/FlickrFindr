package org.br.flickrfinder.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoViewEntity(
        var id: String,
        var title: String,
        var imgOriginal: String
): Parcelable