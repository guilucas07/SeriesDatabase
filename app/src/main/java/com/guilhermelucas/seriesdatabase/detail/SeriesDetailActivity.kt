package com.guilhermelucas.seriesdatabase.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.guilhermelucas.data.datasource.SeriesDataSource
import com.guilhermelucas.data.datasource.TVMazeApi
import com.guilhermelucas.data.utils.RetrofitHelper
import com.guilhermelucas.seriesdatabase.BuildConfig
import com.guilhermelucas.seriesdatabase.R
import com.guilhermelucas.seriesdatabase.base.BaseActivity
import com.guilhermelucas.seriesdatabase.databinding.ActivitySeriesDetailBinding
import com.guilhermelucas.seriesdatabase.detail.data.SeriesDetailRepository
import com.guilhermelucas.seriesdatabase.detail.model.SeriesDetailViewObject
import com.guilhermelucas.seriesdatabase.detail.ui.SeriesDetailPageAdapter
import com.guilhermelucas.seriesdatabase.utils.AndroidResourceProvider
import com.guilhermelucas.seriesdatabase.utils.extensions.getViewModel
import com.guilhermelucas.seriesdatabase.utils.extensions.setupObserver
import com.guilhermelucas.seriesdatabase.utils.models.RequestError
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SeriesDetailActivity : BaseActivity() {

    private lateinit var binding: ActivitySeriesDetailBinding
    private var seriesId: Int = 0
    private val viewModel: SeriesDetailViewModel by viewModel { parametersOf(seriesId) }

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
        setupObserver(showRequestError to ::showRequestErrorMessage)
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