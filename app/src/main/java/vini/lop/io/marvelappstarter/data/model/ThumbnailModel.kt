package vini.lop.io.marvelappstarter.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ThumbnailModel(
    @SerializedName("path") val path: String,
    @SerializedName("extension") val extension: String
) : Parcelable {

    fun getFullPath(): String = "${path}.${extension}".lowercase()
}