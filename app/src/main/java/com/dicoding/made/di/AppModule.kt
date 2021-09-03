package com.dicoding.made.di

import com.dicoding.made.core.domain.usecase.MovieAppInteractor
import com.dicoding.made.core.domain.usecase.MovieAppUseCase
import com.dicoding.made.detail.DetailViewModel
import com.dicoding.made.home.SearchViewModel
import com.dicoding.made.movies.MoviesViewModel
import com.dicoding.made.tvshows.TvShowsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val useCaseModule = module {
    factory<MovieAppUseCase> { MovieAppInteractor(get()) }
}

@ExperimentalCoroutinesApi
@FlowPreview
val viewModelModule = module {
    viewModel { MoviesViewModel(get()) }
    viewModel { TvShowsViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { SearchViewModel(get()) }
}