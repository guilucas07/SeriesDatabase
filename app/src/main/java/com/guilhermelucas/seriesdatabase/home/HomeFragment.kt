package com.guilhermelucas.seriesdatabase.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.guilhermelucas.data.datasource.SeriesDataSource
import com.guilhermelucas.data.datasource.TVMazeApi
import com.guilhermelucas.data.utils.RetrofitHelper
import com.guilhermelucas.seriesdatabase.R
import com.guilhermelucas.seriesdatabase.databinding.FragmentHomeBinding
import com.guilhermelucas.seriesdatabase.home.adapter.AdapterItem
import com.guilhermelucas.seriesdatabase.home.adapter.HomeAdapter
import com.guilhermelucas.seriesdatabase.utils.extensions.getViewModel
import com.guilhermelucas.seriesdatabase.utils.extensions.setupObserver

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private var searchViewMenu: MenuItem? = null

    private val viewModel: HomeViewModel by lazy {
        getViewModel {
            HomeViewModel(
                HomeRepository(
                    SeriesDataSource(
                        RetrofitHelper.createService<TVMazeApi>("https://api.tvmaze.com/")
                    )
                )
            )
        }
    }
    private val adapter =
        HomeAdapter { position ->
            viewModel.onItemClick(position)
        }

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

    private fun initSearchMenu(menu: Menu) {
        searchViewMenu = menu.findItem(R.id.search_action)
        val searchView = (searchViewMenu?.actionView as SearchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null)
                    viewModel.searchMovie(newText)

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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun FragmentHomeBinding.setupView() {
        recyclerViewMovies.adapter = adapter
        recyclerViewMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val directionDown = 1

                if (!recyclerView.canScrollVertically(directionDown)) {
                    viewModel.onAllItemsAreShowed()
                }
            }
        })
        swipeRefreshMovies.setOnRefreshListener {
            viewModel.onSwipeToRefresh()
        }
    }

    private fun setupObserver() = with(viewModel) {
        setupObserver(loadedSeries to ::loadData)
        setupObserver(isLoading to ::isLoadingObserver)
        setupObserver(changeAdapterVisibility to ::changeVisibilityObserver)
    }

    private fun changeVisibilityObserver(state: HomeViewModel.AdapterVisibility) {
        binding?.run {
            when (state) {
                HomeViewModel.AdapterVisibility.DATA_VIEW -> {
                    layoutEmptyMovies.visibility = View.GONE
                    layoutEmptyMoviesSearch.visibility = View.GONE
                    recyclerViewMovies.visibility = View.VISIBLE
                }
                HomeViewModel.AdapterVisibility.SEARCH_EMPTY_VIEW -> {
                    layoutEmptyMovies.visibility = View.GONE
                    layoutEmptyMoviesSearch.visibility = View.VISIBLE
                    recyclerViewMovies.visibility = View.GONE
                }
                else -> {
                    layoutEmptyMovies.visibility = View.VISIBLE
                    layoutEmptyMoviesSearch.visibility = View.GONE
                    recyclerViewMovies.visibility = View.GONE
                }
            }
        }
    }

    private fun isLoadingObserver(isVisible: Boolean) {
        binding?.run {
            if (!isVisible)
                swipeRefreshMovies.isRefreshing = isVisible
        }
    }

    private fun loadData(newData: List<AdapterItem>) {
        adapter.loadItems(newData)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }
}
