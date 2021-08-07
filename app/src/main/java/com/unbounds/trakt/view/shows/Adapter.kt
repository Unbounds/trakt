package com.unbounds.trakt.view.shows

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.unbounds.trakt.databinding.ProgressItemBinding
import com.unbounds.trakt.viewmodel.NextEpisode

/**
 * Created by maclir on 2015-11-17.
 */
class Adapter(private val listener: OnClicked) : ListAdapter<NextEpisode, Adapter.ViewHolder>(object : DiffUtil.ItemCallback<NextEpisode>() {
    override fun areItemsTheSame(oldItem: NextEpisode, newItem: NextEpisode) = oldItem.showId == newItem.showId
    override fun areContentsTheSame(oldItem: NextEpisode, newItem: NextEpisode) = oldItem == newItem
}) {
    interface OnClicked {
        fun onCheckClicked(episode: NextEpisode)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(ProgressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    inner class ViewHolder(private val itemBinding: ProgressItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        private var nextEpisode: NextEpisode? = null

        init {
            itemBinding.progressItemCheck.setOnClickListener(View.OnClickListener {
                val episode = nextEpisode
                if (itemBinding.progressItemCheck.isSelected || episode == null) return@OnClickListener
                itemBinding.progressItemCheck.isSelected = true

                listener.onCheckClicked(episode)
            })
        }

        fun bind(episode: NextEpisode) {
            this.nextEpisode = episode

            with(episode) {
                Picasso.get().load(imageUrl).fit().centerCrop().into(itemBinding.progressItemShowPoster)
                itemBinding.progressItemShowTitle.text = showTitle
                itemBinding.progressItemEpisodeTitle.text = episodeTitle
                itemBinding.progressItemProgressText.text = progressText
                itemBinding.progressItemProgressBar.progress = progress
                itemBinding.progressItemCheck.isSelected = selected
            }
        }
    }
}
