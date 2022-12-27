package vini.lop.io.marvelappstarter.data.model.character

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import vini.lop.io.marvelappstarter.data.model.ThumbnailModel

@Parcelize
data class CharacterModel(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("thumbnail") val thumbnailModel: ThumbnailModel
) : Parcelable