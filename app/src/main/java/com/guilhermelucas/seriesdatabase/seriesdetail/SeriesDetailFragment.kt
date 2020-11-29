package com.guilhermelucas.seriesdatabase.seriesdetail

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.guilhermelucas.data.datasource.ShowDataSource
import com.guilhermelucas.data.datasource.TVMazeApi
import com.guilhermelucas.data.utils.RetrofitHelper
import com.guilhermelucas.seriesdatabase.databinding.FragmentSeriesDetailBinding
import com.guilhermelucas.seriesdatabase.utils.AndroidResourceProvider
import com.guilhermelucas.seriesdatabase.utils.extensions.getViewModel
import com.guilhermelucas.seriesdatabase.utils.extensions.setupObserver

class SeriesDetailFragment : Fragment() {

    private var binding: FragmentSeriesDetailBinding? = null
    private var searchViewMenu: MenuItem? = null
    private val args : SeriesDetailFragmentArgs by navArgs()

    private val viewModel: SeriesDetailViewModel by lazy {
        getViewModel {
            SeriesDetailViewModel(
                args.seriesId,
                SeriesDetailRepository(
                    ShowDataSource(
                        RetrofitHelper.createService<TVMazeApi>("https://api.tvmaze.com/")
                    )
                ),
                AndroidResourceProvider(requireContext())
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSeriesDetailBinding.inflate(inflater).also {
        binding = it
        it.setupView()
        setupObserver()
        SeriesDetailFragmentArgs
    }.root

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun FragmentSeriesDetailBinding.setupView() {
    }

    private fun setupObserver() = with(viewModel) {
        setupObserver(seriesBasicInfo to ::seriesBasicInfoObserver)
    }

    private fun seriesBasicInfoObserver(info: SeriesDetailViewObject) {
        binding?.run {
            exhibitionDetailsText.text = info.exhibitionDescription
            genresText.text = info.genres
            summaryText.text = Html.fromHtml(info.summary)
            seasonsSpinner.adapter = getSeasonsAdapter(info.seasonsList)
            titleText.text = info.name
        }
    }

    private fun getSeasonsAdapter(seasonsList: List<String>?): ArrayAdapter<String> {
        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item,
            seasonsList.orEmpty().toTypedArray()
        )
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return spinnerArrayAdapter
    }

    private fun isLoadingObserver(isVisible: Boolean) {
        binding?.run {

        }
    }

}
