package com.guilhermelucas.seriesdatabase.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.guilhermelucas.data.datasource.SeriesDataSource
import com.guilhermelucas.data.datasource.TVMazeApi
import com.guilhermelucas.data.utils.RetrofitHelper
import com.guilhermelucas.seriesdatabase.BuildConfig
import com.guilhermelucas.seriesdatabase.databinding.ActivitySeriesDetailBinding
import com.guilhermelucas.seriesdatabase.detail.data.SeriesDetailRepository
import com.guilhermelucas.seriesdatabase.detail.model.SeriesDetailViewObject
import com.guilhermelucas.seriesdatabase.detail.ui.SeriesDetailPageAdapter
import com.guilhermelucas.seriesdatabase.utils.AndroidResourceProvider
import com.guilhermelucas.seriesdatabase.utils.extensions.getViewModel
import com.guilhermelucas.seriesdatabase.utils.extensions.setupObserver

class SeriesDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySeriesDetailBinding
    private var seriesId: Int = 0
    private val viewModel: SeriesDetailViewModel by lazy {
        getViewModel {
            SeriesDetailViewModel(
                seriesId,
                SeriesDetailRepository(
                    SeriesDataSource(
                        RetrofitHelper.createService<TVMazeApi>(BuildConfig.API_TV_MAZE)
                    )
                ),
                AndroidResourceProvider(baseContext)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        seriesId = intent.getIntExtra(EXTRA_KEY_SERIES_ID, -1)
        if (seriesId == -1)
            throw IllegalArgumentException(
                "EXTRA_KEY_SERIES_ID should be informed" +
                        "Use newInstance() to ensure a correct initialization"
            )

        binding = ActivitySeriesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupObservers()
    }

    private fun setupToolbar() = with(binding) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setupObservers() = with(viewModel) {
        setupObserver(seriesBasicInfo to ::seriesBasicInfoObserver)
    }

    private fun seriesBasicInfoObserver(series: SeriesDetailViewObject) {
        val sectionsPagerAdapter =
            SeriesDetailPageAdapter(
                supportFragmentManager
            )
        with(binding) {
            viewPager.adapter = sectionsPagerAdapter
            tabLayout.setupWithViewPager(viewPager)
            supportActionBar?.title = series.name
        }
    }

    companion object {
        private const val EXTRA_KEY_SERIES_ID = "extra_series_id"
        fun newInstance(context: Context, seriesId: Int): Intent {
            return Intent(context, SeriesDetailActivity::class.java).apply {
                putExtra(EXTRA_KEY_SERIES_ID, seriesId)
            }
        }
    }
}