package com.example.movies_app.UI.MovieDetail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.movies_app.Data.Api.POSTER_BASE_URL
import com.example.movies_app.Data.Api.TheMovieDbClient
import com.example.movies_app.Data.Api.TheMovieDbInterface
import com.example.movies_app.Data.Repository.NetworkState
import com.example.movies_app.Data.Vo.MovieDetails
import com.example.movies_app.R
import java.text.NumberFormat
import java.util.*

class SingleMovie : AppCompatActivity() {
    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieDetailsRepository
    private lateinit var movieTitle: TextView
    private lateinit var movieTagline: TextView
    private lateinit var movieRelease: TextView
    private lateinit var movieRating: TextView
    private lateinit var movieRuntime: TextView
    private lateinit var movieOverview: TextView
    private lateinit var movieBudget: TextView
    private lateinit var movieRevenue: TextView
    private lateinit var moviePoster: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var textError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)
        assignItems()
        val movieId: Int = intent.getIntExtra("id", 1)
        val apiService: TheMovieDbInterface = TheMovieDbClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)
        viewModel = getViewModel(movieId)
        viewModel.movieDetails.observe(this, Observer {
            bindUi(it)
        })
        viewModel.networkState.observe(this, Observer {
            progressBar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            textError.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    private fun assignItems() {
        movieTitle = findViewById(R.id.movie_title)
        movieTagline = findViewById(R.id.movie_tagline)
        movieRelease = findViewById(R.id.movie_release_date)
        movieRating = findViewById(R.id.movie_rating)
        movieRuntime = findViewById(R.id.movie_runtime)
        movieOverview = findViewById(R.id.movie_overview)
        movieBudget = findViewById(R.id.movie_budget)
        movieRevenue = findViewById(R.id.movie_revenue)
        moviePoster = findViewById(R.id.iv_movie_poster)
        progressBar = findViewById(R.id.progress_bar)
        textError = findViewById(R.id.txt_error)
    }

    @SuppressLint("SetTextI18n")
    private fun bindUi(it: MovieDetails) {
        movieTitle.text = it.title
        movieTagline.text = it.tagline
        movieRelease.text = it.releaseDate
        movieRating.text = it.rating.toString()
        movieRuntime.text = it.runtime.toString() + " minutes"
        movieOverview.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        movieBudget.text = formatCurrency.format(it.budget)
        movieRevenue.text = formatCurrency.format(it.revenue)

        val posterURL: String = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(posterURL)
            .into(moviePoster)

    }

    private fun getViewModel(movieId: Int): SingleMovieViewModel {
        return ViewModelProviders.of(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(movieRepository, movieId) as T
            }
        }) [SingleMovieViewModel::class.java]
    }
}