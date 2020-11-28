package com.guilhermelucas.seriesdatabase.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.guilhermelucas.data.datasource.SeriesDataSource
import com.guilhermelucas.data.datasource.TVMazeApi
import com.guilhermelucas.data.utils.RetrofitHelper
import com.guilhermelucas.seriesdatabase.databinding.FragmentHomeBinding
import com.guilhermelucas.seriesdatabase.home.adapter.AdapterItem
import com.guilhermelucas.seriesdatabase.home.adapter.HomeAdapter
import com.guilhermelucas.seriesdatabase.utils.extensions.getViewModel
import com.guilhermelucas.seriesdatabase.utils.extensions.setupObserver

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null

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
        setupObserver()
    }.root

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

                if(!recyclerView.canScrollVertically(directionDown)){
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
    }

    private fun isLoadingObserver(isVisible : Boolean){
        binding?.run {
            if(!isVisible)
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
