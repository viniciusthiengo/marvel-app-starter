package vini.lop.io.marvelappstarter.data.model.character

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import vini.lop.io.marvelappstarter.data.model.ThumbnailModel

@Entity(tableName = "characterModel")
@Parcelize
data class CharacterModel(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("thumbnail") val thumbnailModel: ThumbnailModel
) : Parcelable