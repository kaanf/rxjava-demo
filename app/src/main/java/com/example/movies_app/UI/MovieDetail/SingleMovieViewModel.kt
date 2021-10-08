package com.example.movies_app.UI.MovieDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.movies_app.Data.Repository.NetworkState
import com.example.movies_app.Data.Vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class SingleMovieViewModel(private val movieRepository: MovieDetailsRepository, movieId: Int): ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val movieDetails: LiveData<MovieDetails> by lazy {
        movieRepository.fetchSingleMovieDetails(compositeDisposable, movieId)
    }
    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.getMovieDetailNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}