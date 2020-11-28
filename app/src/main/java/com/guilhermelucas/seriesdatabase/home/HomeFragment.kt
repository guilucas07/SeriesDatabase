package com.guilhermelucas.seriesdatabase.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.guilhermelucas.data.datasource.SeriesDataSource
import com.guilhermelucas.data.datasource.TVMazeApi
import com.guilhermelucas.data.utils.RetrofitHelper
import com.guilhermelucas.seriesdatabase.R
import com.guilhermelucas.seriesdatabase.home.adapter.AdapterItem
import com.guilhermelucas.seriesdatabase.home.adapter.HomeAdapter
import com.guilhermelucas.seriesdatabase.utils.extensions.getViewModel
import com.guilhermelucas.seriesdatabase.utils.extensions.setupObserver

class HomeFragment : Fragment() {

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
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)
        setupView(root)
        setupObserver()
        return root
    }

    private fun setupView(root: View?) {
        root?.findViewById<RecyclerView>(R.id.recyclerViewMovies)?.adapter = adapter
    }

    private fun setupObserver() = with(viewModel){
        setupObserver(loadedSeries to ::loadData)
    }

    private fun loadData(newData : List<AdapterItem>){
        adapter.loadItems(newData)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }
}