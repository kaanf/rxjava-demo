package com.example.movies_app.UI.MovieDetail

import androidx.lifecycle.LiveData
import com.example.movies_app.Data.Api.TheMovieDbInterface
import com.example.movies_app.Data.Repository.MovieDetailsNetworkDataSource
import com.example.movies_app.Data.Repository.NetworkState
import com.example.movies_app.Data.Vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService: TheMovieDbInterface) {
    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource
    fun fetchSingleMovieDetails (compositeDisposable: CompositeDisposable, movieId: Int): LiveData<MovieDetails> {
        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)
        return movieDetailsNetworkDataSource.downloadMovieResponse
    }
    fun getMovieDetailNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }
}