package com.unbounds.trakt.view.shows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.unbounds.trakt.R
import com.unbounds.trakt.databinding.FragmentProgressBinding
import com.unbounds.trakt.utils.ListState
import com.unbounds.trakt.viewmodel.NextEpisode
import com.unbounds.trakt.viewmodel.ProgressViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProgressFragment : Fragment() {

    @Inject
    lateinit var viewModel: ProgressViewModel

    private var _binding: FragmentProgressBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.progressRecycleView.setHasFixedSize(true)
        val adapter = Adapter(object : Adapter.OnClicked {
            override fun onCheckClicked(episode: NextEpisode) {
                viewModel.episodeWatched(episode)
            }
        })
        binding.progressRecycleView.adapter = adapter
        binding.progressRecycleView.itemAnimator = object : DefaultItemAnimator() {
            init {
                supportsChangeAnimations = false
            }

            override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
                dispatchRemoveFinished(holder)
                return false
            }
        }

        binding.progressSwipeRefreshLayout.setColorSchemeResources(
                R.color.moonlight_blue,
                R.color.boogie_green,
                R.color.space_gray,
                R.color.cool_gray
        )
        binding.progressSwipeRefreshLayout.setOnRefreshListener { viewModel.reload() }

        viewModel.refreshing.observe(viewLifecycleOwner) { state ->
            when (state) {
                ListState.LOADING -> {
                    binding.progressSwipeRefreshLayout.isRefreshing = true
                    binding.progressRecycleView.visibility = View.VISIBLE
                    binding.progressEmptyView.visibility = View.GONE
                }
                ListState.LOADED -> {
                    binding.progressSwipeRefreshLayout.isRefreshing = false
                    binding.progressRecycleView.visibility = View.VISIBLE
                    binding.progressEmptyView.visibility = View.GONE
                }
                ListState.EMPTY -> {
                    binding.progressSwipeRefreshLayout.isRefreshing = false
                    binding.progressRecycleView.visibility = View.GONE
                    binding.progressEmptyView.text = getString(R.string.progress_empty)
                    binding.progressEmptyView.visibility = View.VISIBLE
                }
                ListState.ERROR -> {
                    binding.progressSwipeRefreshLayout.isRefreshing = false
                    binding.progressRecycleView.visibility = View.GONE
                    binding.progressEmptyView.text = getString(R.string.trakt_error)
                    binding.progressEmptyView.visibility = View.VISIBLE
                }
            }
        }

        viewModel.items.observe(viewLifecycleOwner) { episodes ->
            adapter.submitList(episodes)
        }

        viewModel.reload()
    }
}
