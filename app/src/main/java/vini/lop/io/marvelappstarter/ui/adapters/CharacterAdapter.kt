package vini.lop.io.marvelappstarter.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import vini.lop.io.marvelappstarter.R
import vini.lop.io.marvelappstarter.data.model.character.CharacterModel
import vini.lop.io.marvelappstarter.databinding.ItemCharacterBinding
import vini.lop.io.marvelappstarter.util.loadRemoteImg
import vini.lop.io.marvelappstarter.util.preciseSubstring

class CharacterAdapter : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<CharacterModel>() {
        override fun areItemsTheSame(
            oldItem: CharacterModel,
            newItem: CharacterModel
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(
            oldItem: CharacterModel,
            newItem: CharacterModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    var characters: List<CharacterModel>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    inner class CharacterViewHolder(
        val binding: ItemCharacterBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = characters.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacterViewHolder =
        CharacterViewHolder(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        holder: CharacterViewHolder,
        position: Int
    ) {
        val character = characters[position]

        holder.binding.apply {
            tvNameCharacter.text = character.name

            tvDescriptionCharacter.text = if (character.description.trim().isEmpty()) {
                holder.itemView.context.getString(R.string.text_description_empty)
            } else {
                character.description.preciseSubstring(100)
            }

            imgCharacter.loadRemoteImg(path = character.thumbnailModel.getFullPath())
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(character)
            }
        }
    }

    private var onItemClickListener: ((CharacterModel) -> Unit)? = null

    fun setOnClickListener(listener: (CharacterModel) -> Unit) {
        onItemClickListener = listener
    }

    fun getCharacterPosition(position: Int): CharacterModel {
        return characters[position]
    }
}