package com.guilhermelucas.seriesdatabase.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.guilhermelucas.data.datasource.SeriesDataSource
import com.guilhermelucas.data.utils.RetrofitHelper
import com.guilhermelucas.seriesdatabase.BuildConfig
import com.guilhermelucas.seriesdatabase.R
import com.guilhermelucas.seriesdatabase.databinding.ActivityMainBinding
import com.guilhermelucas.seriesdatabase.detail.SeriesDetailActivity
import com.guilhermelucas.seriesdatabase.home.adapter.AdapterItem
import com.guilhermelucas.seriesdatabase.home.adapter.HomeAdapter
import com.guilhermelucas.seriesdatabase.utils.extensions.getViewModel
import com.guilhermelucas.seriesdatabase.utils.extensions.setupObserver

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: HomeViewModel by lazy {
        getViewModel {
            HomeViewModel(
                HomeRepository(
                    SeriesDataSource(
                        RetrofitHelper.createService(BuildConfig.API_TV_MAZE)
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
    private var searchViewMenu: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        if (menu != null)
            initSearchMenu(menu)
        return true
    }

    private fun setupToolbar() = with(binding) {
        setSupportActionBar(appBarMain.toolbar)
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

    private fun setupView() = with(binding.appBarMain.contentMain) {
        setupToolbar()
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
        binding.appBarMain.contentMain.seriesRecycler.scrollToPosition(position)
    }

    private fun goToDetailsObserver(id: Int) {
        startActivity(
            SeriesDetailActivity.newInstance(baseContext, id)
        )
    }

    private fun changeVisibilityObserver(state: HomeViewModel.AdapterVisibility) {
        with(binding.appBarMain.contentMain) {
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
        binding.appBarMain.contentMain.loadingShimmer.run {
            this.isVisible = isVisible
            if (isVisible)
                startShimmer()
            else {
                stopShimmer()
            }
        }
    }

    private fun isLoadingObserver(isVisible: Boolean) {
        if (!isVisible)
            binding.appBarMain.contentMain.seriesSwipe.isRefreshing = isVisible

    }

    private fun loadSeriesObserver(newData: List<AdapterItem>) {
        adapter.loadItems(newData)
    }

    companion object {
        private const val SEARCH_DELAY_TIME = 500L
    }

}
