package com.guilhermelucas.seriesdatabase.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.guilhermelucas.data.datasource.SeriesDataSource
import com.guilhermelucas.data.datasource.TVMazeApi
import com.guilhermelucas.data.utils.RetrofitHelper
import com.guilhermelucas.seriesdatabase.BuildConfig
import com.guilhermelucas.seriesdatabase.R
import com.guilhermelucas.seriesdatabase.databinding.FragmentHomeBinding
import com.guilhermelucas.seriesdatabase.home.adapter.AdapterItem
import com.guilhermelucas.seriesdatabase.home.adapter.HomeAdapter
import com.guilhermelucas.seriesdatabase.detail.SeriesDetailActivity
import com.guilhermelucas.seriesdatabase.utils.extensions.getViewModel
import com.guilhermelucas.seriesdatabase.utils.extensions.setupObserver

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by lazy {
        getViewModel {
            HomeViewModel(
                HomeRepository(
                    SeriesDataSource(
                        RetrofitHelper.createService<TVMazeApi>(BuildConfig.API_TV_MAZE)
                    )
                )
            )
        }
    }

    private val adapter =
        HomeAdapter { position ->
            viewModel.onItemClick(position)
        }

    private val searchHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }
    private var searchRunnable = Runnable { }
    private var binding: FragmentHomeBinding? = null
    private var searchViewMenu: MenuItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentHomeBinding.inflate(inflater).also {
        binding = it
        it.setupView()
        setHasOptionsMenu(true)
        setupObserver()
    }.root

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        initSearchMenu(menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun initSearchMenu(menu: Menu) {
        searchViewMenu = menu.findItem(R.id.search_action)
        val searchView = (searchViewMenu?.actionView as SearchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchHandler.scheduleSeriesSearch {
                    if (newText != null)
                        viewModel.searchMovie(newText)
                }
                return false
            }
        })

        searchViewMenu?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                return viewModel.onCloseSearchBar()
            }

        })
    }

    private fun Handler.scheduleSeriesSearch(block: () -> Unit) {
        removeCallbacks(searchRunnable)
        searchRunnable = Runnable {
            block()
        }
        postDelayed(searchRunnable, SEARCH_DELAY_TIME)
    }

    private fun FragmentHomeBinding.setupView() {
        seriesRecycler.adapter = adapter
        seriesRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val directionDown = 1

                if (!recyclerView.canScrollVertically(directionDown)) {
                    viewModel.onAllItemsAreShowed()
                }
            }
        })
        seriesSwipe.setOnRefreshListener {
            viewModel.onSwipeToRefresh()
            searchViewMenu?.collapseActionView()
        }
    }

    private fun setupObserver() = with(viewModel) {
        setupObserver(loadedSeries to ::loadSeriesObserver)
        setupObserver(isLoading to ::isLoadingObserver)
        setupObserver(goToDetails to ::goToDetailsObserver)
        setupObserver(changeAdapterVisibility to ::changeVisibilityObserver)
        setupObserver(scrollToPosition to ::scrollPositionObserver)
    }

    private fun scrollPositionObserver(position: Int) {
        binding?.run {
            seriesRecycler.scrollToPosition(position)
        }
    }

    private fun goToDetailsObserver(id: Int) {
//        val action = HomeFragmentDirections.actionNavHomeToNavGallery(id)
//        findNavController().navigate(action)
        startActivity(
            SeriesDetailActivity.newInstance(requireContext(), id)
        )
    }

    private fun changeVisibilityObserver(state: HomeViewModel.AdapterVisibility) {
        binding?.run {
            when (state) {
                HomeViewModel.AdapterVisibility.DATA_VIEW -> {
                    emptyDataText.isVisible = false
                    emptySearchText.isVisible = false
                    seriesRecycler.isVisible = true
                    disableShimmer()
                }
                HomeViewModel.AdapterVisibility.SEARCH_EMPTY_VIEW -> {
                    emptyDataText.isVisible = false
                    emptySearchText.isVisible = true
                    seriesRecycler.isVisible = false
                    disableShimmer()
                }
                HomeViewModel.AdapterVisibility.SHIMMER_LOADING -> {
                    emptyDataText.isVisible = false
                    emptySearchText.isVisible = false
                    seriesRecycler.isVisible = false
                    enableShimmer()
                }
                else -> {
                    emptyDataText.isVisible = true
                    emptySearchText.isVisible = false
                    seriesRecycler.isVisible = false
                    disableShimmer()
                }
            }
        }
    }

    private fun disableShimmer() {
        controlShimmer(false)
    }

    private fun enableShimmer() {
        controlShimmer(true)
    }

    private fun controlShimmer(isVisible: Boolean) {
        binding?.loadingShimmer?.run {
            this.isVisible = isVisible
            if (isVisible)
                startShimmer()
            else {
                stopShimmer()
            }
        }
    }

    private fun isLoadingObserver(isVisible: Boolean) {
        binding?.run {
            if (!isVisible)
                seriesSwipe.isRefreshing = isVisible
        }
    }

    private fun loadSeriesObserver(newData: List<AdapterItem>) {
        adapter.loadItems(newData)
    }

    companion object {
        private const val SEARCH_DELAY_TIME = 500L
    }
}
