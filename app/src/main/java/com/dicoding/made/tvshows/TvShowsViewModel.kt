package com.dicoding.made.tvshows

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.made.core.data.Resource
import com.dicoding.made.core.domain.model.Movie
import com.dicoding.made.core.domain.usecase.MovieAppUseCase

class TvShowsViewModel(private val movieAppUseCase: MovieAppUseCase) : ViewModel() {
    fun getTvShows(sort: String): LiveData<Resource<List<Movie>>> =
        movieAppUseCase.getAllTvShows(sort).asLiveData()
}