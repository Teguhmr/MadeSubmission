package com.dicoding.made.tvshows

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.made.R
import com.dicoding.made.core.data.Resource
import com.dicoding.made.core.domain.model.Movie
import com.dicoding.made.core.ui.MoviesAdapter
import com.dicoding.made.core.utils.SortUtils
import com.dicoding.made.databinding.FragmentTvShowsBinding
import com.dicoding.made.detail.DetailActivity
import com.dicoding.made.home.HomeActivity
import com.dicoding.made.home.SearchViewModel
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
@FlowPreview
class TvShowsFragment : Fragment() {

    private var fragmentTvShowsBinding: FragmentTvShowsBinding? = null
    private val binding get() = fragmentTvShowsBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentTvShowsBinding = FragmentTvShowsBinding.inflate(inflater, container, false)
        val toolbar: Toolbar = activity?.findViewById<View>(R.id.toolbar) as Toolbar
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        searchView = (activity as HomeActivity).findViewById(R.id.search_view)
        return binding.root
    }

    private val viewModel: TvShowsViewModel by viewModel()
    private lateinit var tvShowsAdapter: MoviesAdapter
    private val searchViewModel: SearchViewModel by viewModel()
    private var searchView: MaterialSearchView? = null
    private var sort = SortUtils.NEWEST

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvShowsAdapter = MoviesAdapter()
        setList(sort)
        observeSearchQuery()
        setSearchList()

        with(binding.rvMovies) {
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = tvShowsAdapter
        }

        tvShowsAdapter.onItemClick = { selectedData ->
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_MOVIE, selectedData)
            startActivity(intent)
        }

        binding.random.setOnClickListener {
            binding.menu.close(true)
            sort = SortUtils.RANDOM
            setList(sort)
        }
        binding.newest.setOnClickListener {
            binding.menu.close(true)
            sort = SortUtils.NEWEST
            setList(sort)
        }
        binding.popularity.setOnClickListener {
            binding.menu.close(true)
            sort = SortUtils.POPULARITY
            setList(sort)
        }
        binding.vote.setOnClickListener {
            binding.menu.close(true)
            sort = SortUtils.VOTE
            setList(sort)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val item = menu.findItem(R.id.action_search)
        searchView!!.setMenuItem(item)
    }

    private fun setList(sort: String) {
        viewModel.getTvShows(sort).observe(this, tvShowsObserver)
    }

    private val tvShowsObserver = Observer<Resource<List<Movie>>> { tvShow ->
        if (tvShow != null) {
            when (tvShow) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.notFound.visibility = View.GONE
                    binding.notFoundText.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.notFound.visibility = View.GONE
                    binding.notFoundText.visibility = View.GONE
                    tvShowsAdapter.setData(tvShow.data)
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.notFound.visibility = View.VISIBLE
                    binding.notFoundText.visibility = View.VISIBLE
                    Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeSearchQuery() {
        searchView!!.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    searchViewModel.setSearchQuery(it)
                }
                return true
            }

        })
    }

    private fun setSearchList() {
        searchViewModel.tvShowResult.observe(viewLifecycleOwner, { tvShows ->
            if (tvShows.isNullOrEmpty()) {
                binding.progressBar.visibility = View.GONE
                binding.notFound.visibility = View.VISIBLE
                binding.notFoundText.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.notFound.visibility = View.GONE
                binding.notFoundText.visibility = View.GONE
            }
            tvShowsAdapter.setData(tvShows)
        })
        searchView!!.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewShown() {
                binding.progressBar.visibility = View.GONE
                binding.notFound.visibility = View.GONE
                binding.notFoundText.visibility = View.GONE
            }

            override fun onSearchViewClosed() {
                binding.progressBar.visibility = View.GONE
                binding.notFound.visibility = View.GONE
                binding.notFoundText.visibility = View.GONE
                setList(sort)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView = null
        fragmentTvShowsBinding = null
    }
}