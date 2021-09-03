package com.dicoding.made.detail

import androidx.lifecycle.ViewModel
import com.dicoding.made.core.domain.model.Movie
import com.dicoding.made.core.domain.usecase.MovieAppUseCase

class DetailViewModel(private val movieAppUseCase: MovieAppUseCase) : ViewModel() {

    fun setFavoriteMovie(movie: Movie, newStatus: Boolean) {
        movieAppUseCase.setMovieFavorite(movie, newStatus)
    }
}
