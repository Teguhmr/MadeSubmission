package com.dicoding.made.favorite.movies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.made.core.domain.model.Movie
import com.dicoding.made.core.ui.MoviesAdapter
import com.dicoding.made.core.utils.SortUtils
import com.dicoding.made.detail.DetailActivity
import com.dicoding.made.favorite.FavoriteViewModel
import com.dicoding.made.favorite.R
import com.dicoding.made.favorite.databinding.FragmentFavoriteMoviesBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.android.viewmodel.ext.android.viewModel

class FavoriteMoviesFragment : Fragment() {
    private var _fragmentFavoriteMoviesBinding: FragmentFavoriteMoviesBinding? = null
    private val binding get() = _fragmentFavoriteMoviesBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentFavoriteMoviesBinding =
            FragmentFavoriteMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var moviesAdapter: MoviesAdapter
    private val viewModel: FavoriteViewModel by viewModel()
    private var sort = SortUtils.NEWEST

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemTouchHelper.attachToRecyclerView(binding.rvFavoriteMovies)

        moviesAdapter = MoviesAdapter()

        binding.progressBar.visibility = View.VISIBLE
        binding.notFound.visibility = View.GONE
        binding.notFoundText.visibility = View.GONE
        setList(sort)

        with(binding.rvFavoriteMovies) {
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
            this.adapter = moviesAdapter
        }

        moviesAdapter.onItemClick = { selectedData ->
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_MOVIE, selectedData)
            startActivity(intent)
        }
    }

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            return makeMovementFlags(0, ItemTouchHelper.RIGHT)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if (view != null) {
                val swipedPosition = viewHolder.adapterPosition
                val movie = moviesAdapter.getSwipedData(swipedPosition)
                var state = movie.favorite
                viewModel.setFavorite(movie, !state)
                state = !state
                val snackBar =
                    Snackbar.make(view as View, R.string.message_undo, Snackbar.LENGTH_LONG)
                snackBar.setAction(R.string.message_ok) {
                    viewModel.setFavorite(movie, !state)
                }
                snackBar.show()
            }
        }
    })

    private fun setList(sort: String) {
        viewModel.getFavoriteMovies(sort).observe(this, moviesObserver)
    }

    private val moviesObserver = Observer<List<Movie>> { movies ->
        if (movies.isNullOrEmpty()){
            binding.progressBar.visibility = View.GONE
            binding.notFound.visibility = View.VISIBLE
            binding.notFoundText.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.notFound.visibility = View.GONE
            binding.notFoundText.visibility = View.GONE
        }
        moviesAdapter.setData(movies)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentFavoriteMoviesBinding = null
    }

}