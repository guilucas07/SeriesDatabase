package com.guilhermelucas.seriesdatabase.detail.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.guilhermelucas.seriesdatabase.databinding.FragmentSeriesOverviewBinding
import com.guilhermelucas.seriesdatabase.detail.SeriesDetailViewModel
import com.guilhermelucas.seriesdatabase.detail.model.SeriesDetailViewObject
import com.guilhermelucas.seriesdatabase.utils.extensions.loadImage
import com.guilhermelucas.seriesdatabase.utils.extensions.setupObserver
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel

class SeriesOverviewFragment : Fragment() {

    private var binding: FragmentSeriesOverviewBinding? = null

//    private val viewModel: SeriesDetailViewModel by lazy {
//        ViewModelProvider(requireActivity()).get(SeriesDetailViewModel::class.java)
//    }
    private val viewModel: SeriesDetailViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSeriesOverviewBinding.inflate(inflater).also {
        binding = it
        setupObservers()
    }.root

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupInformation(series: SeriesDetailViewObject) {
        binding?.run {
            exhibitionDetailsText.text = series.exhibitionDescription
            genresText.text = series.genres
            summaryText.text = series.summary
            posterImage.loadImage(series.imageUrl)
        }
    }

    private fun setupObservers() = with(viewModel) {
        setupObserver(seriesBasicInfo to ::setupInformation)
    }

}
