package com.unbounds.trakt.view.progress

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.unbounds.trakt.R
import com.unbounds.trakt.viewmodel.NextEpisode
import kotlinx.android.synthetic.main.progress_item.view.*

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.inflate(R.layout.progress_item))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private var nextEpisode: NextEpisode? = null

        init {
            view.progress_item_check.setOnClickListener(View.OnClickListener {
                val episode = nextEpisode
                if (view.progress_item_check.isSelected || episode == null) return@OnClickListener
                view.progress_item_check.isSelected = true

                listener.onCheckClicked(episode)
            })
        }

        fun bind(episode: NextEpisode) {
            this.nextEpisode = episode

            with(episode) {
                Picasso.get().load(imageUrl).fit().into(view.progress_item_show_poster)
                view.progress_item_show_title.text = showTitle
                view.progress_item_episode_title.text = episodeTitle
                view.progress_item_progress_text.text = progressText
                view.progress_item_progress_bar.progress = progress
                view.progress_item_check.isSelected = selected
            }
        }
    }
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}
