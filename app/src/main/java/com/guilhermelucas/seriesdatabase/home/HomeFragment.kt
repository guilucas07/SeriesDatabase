package com.guilhermelucas.seriesdatabase.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.guilhermelucas.data.datasource.ShowDataSource
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
                    ShowDataSource(
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
        recyclerViewShows.adapter = adapter
        recyclerViewShows.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val directionDown = 1

                if (!recyclerView.canScrollVertically(directionDown)) {
                    viewModel.onAllItemsAreShowed()
                }
            }
        })
        swipeRefreshShows.setOnRefreshListener {
            viewModel.onSwipeToRefresh()
            searchViewMenu?.collapseActionView()
        }
    }

    private fun setupObserver() = with(viewModel) {
        setupObserver(loadedSeries to ::loadData)
        setupObserver(isLoading to ::isLoadingObserver)
        setupObserver(goToDetails to ::goToDetailsObserver)
        setupObserver(changeAdapterVisibility to ::changeVisibilityObserver)
    }

    private fun goToDetailsObserver(id: Int) {
        val action = HomeFragmentDirections.actionNavHomeToNavGallery(id)
        findNavController().navigate(action)
    }

    private fun changeVisibilityObserver(state: HomeViewModel.AdapterVisibility) {
        binding?.run {
            when (state) {
                HomeViewModel.AdapterVisibility.DATA_VIEW -> {
                    layoutEmptyShows.isVisible = false
                    layoutEmptyShowsSearch.isVisible = false
                    recyclerViewShows.isVisible = true
                }
                HomeViewModel.AdapterVisibility.SEARCH_EMPTY_VIEW -> {
                    layoutEmptyShows.isVisible = false
                    layoutEmptyShowsSearch.isVisible = true
                    recyclerViewShows.isVisible = false
                }
                else -> {
                    layoutEmptyShows.isVisible = true
                    layoutEmptyShowsSearch.isVisible = false
                    recyclerViewShows.isVisible = false
                }
            }
        }
    }

    private fun isLoadingObserver(isVisible: Boolean) {
        binding?.run {
            if (!isVisible)
                swipeRefreshShows.isRefreshing = isVisible
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
