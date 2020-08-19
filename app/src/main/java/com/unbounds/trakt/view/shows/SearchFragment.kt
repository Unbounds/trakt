package com.unbounds.trakt.view.shows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.unbounds.trakt.R
import com.unbounds.trakt.viewmodel.NextEpisode
import com.unbounds.trakt.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    @Inject
    lateinit var viewModel: SearchViewModel

    private val args: SearchFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        search_recycle_view.setHasFixedSize(true)
        val adapter = Adapter(object : Adapter.OnClicked {
            override fun onCheckClicked(episode: NextEpisode) {
                viewModel.episodeWatched(episode)
            }
        })
        search_recycle_view.adapter = adapter
        search_recycle_view.itemAnimator = object : DefaultItemAnimator() {
            init {
                supportsChangeAnimations = false
            }

            override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
                dispatchRemoveFinished(holder)
                return false
            }
        }

        search_swipe_refresh_layout.setColorSchemeResources(
                R.color.moonlight_blue,
                R.color.boogie_green,
                R.color.space_gray,
                R.color.cool_gray
        )

        viewModel.refreshing.observe(viewLifecycleOwner) { refreshing ->
            search_swipe_refresh_layout.isRefreshing = refreshing
        }

        var firstItem: NextEpisode? = null
        viewModel.items.observe(viewLifecycleOwner) { episodes ->
            adapter.submitList(episodes) {
                if (firstItem == episodes.firstOrNull()) return@submitList
                firstItem = episodes.firstOrNull()
                with(search_recycle_view) {
                    if (!hasNestedScrollingParent(ViewCompat.TYPE_NON_TOUCH)) {
                        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH);
                    }
                    smoothScrollToPosition(0)
                }
            }
        }

        viewModel.search(args.query)
    }
}
