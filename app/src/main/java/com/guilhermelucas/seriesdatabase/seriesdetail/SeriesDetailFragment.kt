package com.guilhermelucas.seriesdatabase.seriesdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.guilhermelucas.data.datasource.SeriesDataSource
import com.guilhermelucas.data.datasource.TVMazeApi
import com.guilhermelucas.data.utils.RetrofitHelper
import com.guilhermelucas.seriesdatabase.R
import com.guilhermelucas.seriesdatabase.databinding.FragmentSeriesDetailBinding
import com.guilhermelucas.seriesdatabase.seriesdetail.adapter.SeriesDetailEpisodesAdapter
import com.guilhermelucas.seriesdatabase.seriesdetail.adapter.model.EpisodeViewObject
import com.guilhermelucas.seriesdatabase.utils.AndroidResourceProvider
import com.guilhermelucas.seriesdatabase.utils.extensions.getViewModel
import com.guilhermelucas.seriesdatabase.utils.extensions.loadImage
import com.guilhermelucas.seriesdatabase.utils.extensions.setupObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class SeriesDetailFragment : Fragment() {

    private var binding: FragmentSeriesDetailBinding? = null
    private val args: SeriesDetailFragmentArgs by navArgs()
    private val adapter: SeriesDetailEpisodesAdapter by lazy {
        SeriesDetailEpisodesAdapter(
            CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        )
    }

    private val viewModel: SeriesDetailViewModel by lazy {
        getViewModel {
            SeriesDetailViewModel(
                args.seriesId,
                SeriesDetailRepository(
                    SeriesDataSource(
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
        setupObserver(seriesBasicInfo to ::seriesBasicInfoObserver)
        setupObserver(seasonEpisodes to ::seasonEpisodesObserver)
        setupObserver(isLoadingSeasonEpisodes to ::isLoadingSeasonEpisodesObserver)
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

    private fun seriesBasicInfoObserver(info: SeriesDetailViewObject) {
        binding?.run {
            exhibitionDetailsText.text = info.exhibitionDescription
            genresText.text = info.genres
            summaryText.text = info.summary
            seasonsSpinner.adapter = getSeasonsAdapter(info.seasonsList)
            titleText.text = info.name
            posterImage.loadImage(info.imageUrl)
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

    private fun isLoadingObserver(isVisible: Boolean) {
        //TODO handle progress visibility
    }

}
