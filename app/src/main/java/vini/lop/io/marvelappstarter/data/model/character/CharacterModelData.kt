package vini.lop.io.marvelappstarter.data.model.character

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class CharacterModelData(
    @SerializedName("results") val results: List<CharacterModel>
) : Parcelable