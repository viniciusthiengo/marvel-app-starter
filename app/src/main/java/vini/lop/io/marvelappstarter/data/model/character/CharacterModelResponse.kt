package vini.lop.io.marvelappstarter.data.model.character

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class CharacterModelResponse(
    @SerializedName("data") val data: CharacterModelData
) : Parcelable