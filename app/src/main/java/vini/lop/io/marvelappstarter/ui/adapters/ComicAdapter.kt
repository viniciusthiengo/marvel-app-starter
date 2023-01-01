package vini.lop.io.marvelappstarter.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import vini.lop.io.marvelappstarter.data.model.comic.ComicModel
import vini.lop.io.marvelappstarter.databinding.ItemComicBinding
import vini.lop.io.marvelappstarter.util.loadRemoteImg

class ComicAdapter : RecyclerView.Adapter<ComicAdapter.ComicViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<ComicModel>() {
        override fun areItemsTheSame(
            oldItem: ComicModel,
            newItem: ComicModel
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(
            oldItem: ComicModel,
            newItem: ComicModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    var comics: List<ComicModel>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    inner class ComicViewHolder(
        val binding: ItemComicBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = comics.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ComicViewHolder =
        ComicViewHolder(
            ItemComicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        holder: ComicViewHolder,
        position: Int
    ) {
        val comic = comics[position]

        holder.binding.apply {
            tvNameComic.text = comic.title
            tvDescriptionComic.text = comic.description

            imgComic.loadRemoteImg(path = comic.thumbnailModel.getFullPath())
        }
    }
}