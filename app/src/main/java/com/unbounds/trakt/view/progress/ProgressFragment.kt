package com.unbounds.trakt.view.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.unbounds.trakt.R
import com.unbounds.trakt.viewmodel.NextEpisode
import com.unbounds.trakt.viewmodel.ProgressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_progress.*
import javax.inject.Inject

@AndroidEntryPoint
class ProgressFragment : Fragment() {

    @Inject
    lateinit var viewModel: ProgressViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.activity_progress, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progress_recycle_view.setHasFixedSize(true)
        val adapter = Adapter(object : Adapter.OnClicked {
            override fun onCheckClicked(episode: NextEpisode) {
                viewModel.episodeWatched(episode)
            }
        })
        progress_recycle_view.adapter = adapter
        progress_recycle_view.itemAnimator = object : DefaultItemAnimator() {
            init {
                supportsChangeAnimations = false
            }

            override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
                dispatchRemoveFinished(holder)
                return false
            }
        }

        progress_swipe_refresh_layout.setColorSchemeResources(
                R.color.moonlight_blue,
                R.color.boogie_green,
                R.color.space_gray,
                R.color.cool_gray
        )
        progress_swipe_refresh_layout.setOnRefreshListener { viewModel.reload() }

        viewModel.refreshing.observe(viewLifecycleOwner, Observer { refreshing ->
            progress_swipe_refresh_layout.isRefreshing = refreshing
        })

        viewModel.items.observe(viewLifecycleOwner, Observer { episodes ->
            adapter.submitList(episodes)
        })

        viewModel.reload()
    }
}
