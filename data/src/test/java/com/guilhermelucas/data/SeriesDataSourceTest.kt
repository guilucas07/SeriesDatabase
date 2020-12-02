package com.guilhermelucas.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.guilhermelucas.data.datasource.SeriesDataSource
import com.guilhermelucas.data.datasource.TVMazeApi
import com.guilhermelucas.data.model.SeriesResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import java.lang.reflect.Type

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class SeriesDataSourceTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    private val api: TVMazeApi = mockk(relaxed = true)
    private var dataSource = SeriesDataSource(api)
    private val validSeriesJsonText =
        "[ { \"id\": 1, \"url\": \"http://www.tvmaze.com/shows/1/under-the-dome\", \"name\": \"Under the Dome\", \"type\": \"Scripted\", \"language\": \"English\", \"genres\": [ \"Drama\", \"Science-Fiction\", \"Thriller\" ], \"status\": \"Ended\", \"runtime\": 60, \"premiered\": \"2013-06-24\", \"officialSite\": \"http://www.cbs.com/shows/under-the-dome/\", \"schedule\": { \"time\": \"22:00\", \"days\": [ \"Thursday\" ] }, \"rating\": { \"average\": 6.5 }, \"weight\": 97, \"network\": { \"id\": 2, \"name\": \"CBS\", \"country\": { \"name\": \"United States\", \"code\": \"US\", \"timezone\": \"America/New_York\" } }, \"webChannel\": null, \"externals\": { \"tvrage\": 25988, \"thetvdb\": 264492, \"imdb\": \"tt1553656\" }, \"image\": { \"medium\": \"http://static.tvmaze.com/uploads/images/medium_portrait/81/202627.jpg\", \"original\": \"http://static.tvmaze.com/uploads/images/original_untouched/81/202627.jpg\" }, \"summary\": \"<p><b>Under the Dome</b> is the story of a small town that is suddenly and inexplicably sealed off from the rest of the world by an enormous transparent dome. The town's inhabitants must deal with surviving the post-apocalyptic conditions while searching for answers about the dome, where it came from and if and when it will go away.</p>\", \"updated\": 1573667713, \"_links\": { \"self\": { \"href\": \"http://api.tvmaze.com/shows/1\" }, \"previousepisode\": { \"href\": \"http://api.tvmaze.com/episodes/185054\" } } }, { \"id\": 2, \"url\": \"http://www.tvmaze.com/shows/2/person-of-interest\", \"name\": \"Person of Interest\", \"type\": \"Scripted\", \"language\": \"English\", \"genres\": [ \"Action\", \"Crime\", \"Science-Fiction\" ], \"status\": \"Ended\", \"runtime\": 60, \"premiered\": \"2011-09-22\", \"officialSite\": \"http://www.cbs.com/shows/person_of_interest/\", \"schedule\": { \"time\": \"22:00\", \"days\": [ \"Tuesday\" ] }, \"rating\": { \"average\": 8.9 }, \"weight\": 95, \"network\": { \"id\": 2, \"name\": \"CBS\", \"country\": { \"name\": \"United States\", \"code\": \"US\", \"timezone\": \"America/New_York\" } }, \"webChannel\": null, \"externals\": { \"tvrage\": 28376, \"thetvdb\": 248742, \"imdb\": \"tt1839578\" }, \"image\": { \"medium\": \"http://static.tvmaze.com/uploads/images/medium_portrait/163/407679.jpg\", \"original\": \"http://static.tvmaze.com/uploads/images/original_untouched/163/407679.jpg\" }, \"summary\": \"<p>You are being watched. The government has a secret system, a machine that spies on you every hour of every day. I know because I built it. I designed the Machine to detect acts of terror but it sees everything. Violent crimes involving ordinary people. People like you. Crimes the government considered \\\"irrelevant\\\". They wouldn't act so I decided I would. But I needed a partner. Someone with the skills to intervene. Hunted by the authorities, we work in secret. You'll never find us. But victim or perpetrator, if your number is up, we'll find you.</p>\", \"updated\": 1588773151, \"_links\": { \"self\": { \"href\": \"http://api.tvmaze.com/shows/2\" }, \"previousepisode\": { \"href\": \"http://api.tvmaze.com/episodes/659372\" } } }, { \"id\": 3, \"url\": \"http://www.tvmaze.com/shows/3/bitten\", \"name\": \"Bitten\", \"type\": \"Scripted\", \"language\": \"English\", \"genres\": [ \"Drama\", \"Horror\", \"Romance\" ], \"status\": \"Ended\", \"runtime\": 60, \"premiered\": \"2014-01-11\", \"officialSite\": \"http://bitten.space.ca/\", \"schedule\": { \"time\": \"22:00\", \"days\": [ \"Friday\" ] }, \"rating\": { \"average\": 7.5 }, \"weight\": 88, \"network\": { \"id\": 7, \"name\": \"CTV Sci-Fi Channel\", \"country\": { \"name\": \"Canada\", \"code\": \"CA\", \"timezone\": \"America/Halifax\" } }, \"webChannel\": null, \"externals\": { \"tvrage\": 34965, \"thetvdb\": 269550, \"imdb\": \"tt2365946\" }, \"image\": { \"medium\": \"http://static.tvmaze.com/uploads/images/medium_portrait/0/15.jpg\", \"original\": \"http://static.tvmaze.com/uploads/images/original_untouched/0/15.jpg\" }, \"summary\": \"<p>Based on the critically acclaimed series of novels from Kelley Armstrong. Set in Toronto and upper New York State, <b>Bitten</b> follows the adventures of 28-year-old Elena Michaels, the world's only female werewolf. An orphan, Elena thought she finally found her \\\"happily ever after\\\" with her new love Clayton, until her life changed forever. With one small bite, the normal life she craved was taken away and she was left to survive life with the Pack.</p>\", \"updated\": 1603936716, \"_links\": { \"self\": { \"href\": \"http://api.tvmaze.com/shows/3\" }, \"previousepisode\": { \"href\": \"http://api.tvmaze.com/episodes/631862\" } } } ]"

    @Test
    fun `when receive a valid json from api should return a valid list of series`() {
        /* given */
        val type: Type = Types.newParameterizedType(
            List::class.java,
            SeriesResponse::class.java
        )
        val adapter: JsonAdapter<List<SeriesResponse>> = Moshi.Builder().build().adapter(type)
        val listReturn = adapter.fromJson(validSeriesJsonText)!!
        coEvery {
            api.getAllSeries(any())
        } returns listReturn

        coroutineScope.launch {
            /* when */
            val allItems = dataSource.loadAll(0)

            /* then */
            assert(allItems.size == 3)
            assert(allItems[0].name == listReturn[0].name)
        }
    }
}
