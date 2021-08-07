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
import com.unbounds.trakt.databinding.FragmentSearchBinding
import com.unbounds.trakt.utils.ListState
import com.unbounds.trakt.viewmodel.NextEpisode
import com.unbounds.trakt.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    @Inject
    lateinit var viewModel: SearchViewModel

    private val args: SearchFragmentArgs by navArgs()

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.searchRecycleView.setHasFixedSize(true)
        val adapter = Adapter(object : Adapter.OnClicked {
            override fun onCheckClicked(episode: NextEpisode) {
                viewModel.episodeWatched(episode)
            }
        })
        binding.searchRecycleView.adapter = adapter
        binding.searchRecycleView.itemAnimator = object : DefaultItemAnimator() {
            init {
                supportsChangeAnimations = false
            }

            override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
                dispatchRemoveFinished(holder)
                return false
            }
        }

        binding.searchSwipeRefreshLayout.setColorSchemeResources(
                R.color.moonlight_blue,
                R.color.boogie_green,
                R.color.space_gray,
                R.color.cool_gray
        )

        viewModel.refreshing.observe(viewLifecycleOwner) { state ->
            when (state) {
                ListState.LOADING -> {
                    binding.searchSwipeRefreshLayout.isRefreshing = true
                    binding.searchRecycleView.visibility = View.VISIBLE
                    binding.searchEmptyView.visibility = View.GONE
                }
                ListState.LOADED -> {
                    binding.searchSwipeRefreshLayout.isRefreshing = false
                    binding.searchRecycleView.visibility = View.VISIBLE
                    binding.searchEmptyView.visibility = View.GONE
                }
                ListState.EMPTY -> {
                    binding.searchSwipeRefreshLayout.isRefreshing = false
                    binding.searchRecycleView.visibility = View.GONE
                    binding.searchEmptyView.visibility = View.VISIBLE
                }
            }
        }

        var firstItem: NextEpisode? = null
        viewModel.items.observe(viewLifecycleOwner) { episodes ->
            adapter.submitList(episodes) {
                if (firstItem == episodes.firstOrNull()) return@submitList
                firstItem = episodes.firstOrNull()
                with(binding.searchRecycleView) {
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
