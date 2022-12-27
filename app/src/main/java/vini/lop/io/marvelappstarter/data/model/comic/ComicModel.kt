package vini.lop.io.marvelappstarter.data.model.comic

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import vini.lop.io.marvelappstarter.data.model.ThumbnailModel

@Parcelize
data class ComicModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("thumbnail")
    val thumbnailModel: ThumbnailModel
) : Parcelable