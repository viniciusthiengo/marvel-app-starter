package vini.lop.io.marvelappstarter.data.model.comic

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class ComicModelData(
    @SerializedName("results")
    val result: List<ComicModel>
) : Parcelable