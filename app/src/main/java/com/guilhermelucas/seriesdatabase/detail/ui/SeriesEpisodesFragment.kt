package com.guilhermelucas.seriesdatabase.detail.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.guilhermelucas.seriesdatabase.R
import com.guilhermelucas.seriesdatabase.databinding.FragmentSeriesEpisodesBinding
import com.guilhermelucas.seriesdatabase.detail.SeriesDetailViewModel
import com.guilhermelucas.seriesdatabase.detail.model.SeriesDetailViewObject
import com.guilhermelucas.seriesdatabase.detail.ui.adapter.SeriesDetailEpisodesAdapter
import com.guilhermelucas.seriesdatabase.detail.ui.adapter.model.EpisodeViewObject
import com.guilhermelucas.seriesdatabase.utils.extensions.setupObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.viewmodel.ext.android.sharedViewModel

class SeriesEpisodesFragment : Fragment() {

    private var binding: FragmentSeriesEpisodesBinding? = null
    private val adapter: SeriesDetailEpisodesAdapter by lazy {
        SeriesDetailEpisodesAdapter(
            CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        )
    }

    private val viewModel: SeriesDetailViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSeriesEpisodesBinding.inflate(inflater).also {
        binding = it
        it.setupView()
        setupObserver()
    }.root

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun FragmentSeriesEpisodesBinding.setupView() {
        seasonEpisodesRecycler.adapter = adapter
        seasonsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //nothing yet
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.onSpinnerSeasonUpdated(position)
            }
        }
    }

    private fun setupObserver() = with(viewModel) {
        setupObserver(seasonEpisodes to ::seasonEpisodesObserver)
        setupObserver(isLoadingSeasonEpisodes to ::isLoadingSeasonEpisodesObserver)
        setupObserver(seriesBasicInfo to ::setupInformation)
    }

    private fun isLoadingSeasonEpisodesObserver(isVisible: Boolean) {
        binding?.run {
            seasonEpisodesProgress.isVisible = isVisible
            seasonEpisodesRecycler.visibility = if (isVisible) View.INVISIBLE else View.VISIBLE
        }
    }

    private fun seasonEpisodesObserver(list: List<EpisodeViewObject>) {
        adapter.loadItems(list)
    }

    private fun setupInformation(series: SeriesDetailViewObject) {
        binding?.seasonsSpinner?.run {
            adapter = getSeasonsAdapter(series.seasonsList)
            isVisible = series.seasonsList?.size ?: 0 > 1
        }
    }

    private fun getSeasonsAdapter(seasonsList: List<String>?): ArrayAdapter<String> {
        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(), R.layout.adapter_spinner_episode_item,
            seasonsList.orEmpty().toTypedArray()
        )
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return spinnerArrayAdapter
    }

}
