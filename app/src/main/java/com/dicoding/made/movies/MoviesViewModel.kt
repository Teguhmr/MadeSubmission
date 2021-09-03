package com.dicoding.made.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.made.core.data.Resource
import com.dicoding.made.core.domain.model.Movie
import com.dicoding.made.core.domain.usecase.MovieAppUseCase

class MoviesViewModel(private val movieAppUseCase: MovieAppUseCase) : ViewModel() {
    fun getMovies(sort: String): LiveData<Resource<List<Movie>>> {
        return movieAppUseCase.getAllMovies(sort).asLiveData()
    }
}