package com.dicoding.made.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.dicoding.made.R
import com.dicoding.made.core.domain.model.Movie
import com.dicoding.made.databinding.ActivityDetailBinding
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {


    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailMovie = intent.getParcelableExtra<Movie>(EXTRA_MOVIE)
        if (detailMovie != null) {
            populateDetail(detailMovie)
        }

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Detail Movie"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun populateDetail(movie: Movie) {
        with(binding) {
            tv_item_title.text = movie.title
            tv_item_language.text = movie.originalLanguage
            val time: String = movie.releaseDate
            @SuppressLint("SimpleDateFormat")
            val parser = SimpleDateFormat("yyyy-MM-dd")
            var date: Date? = null
            try {
                date = parser.parse(time)
                @SuppressLint("SimpleDateFormat")
                val formatter =
                    SimpleDateFormat("EEEE, d MMM yyyy")
                var formattedDate: String? = null
                if (date != null) {
                    formattedDate = formatter.format(date)
                }
                release.text = formattedDate
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            description.text = movie.overview
            tv_item_vote_count.text = movie.popularity.toString()

            tv_item_rating.text = movie.voteAverage.toString()
            Glide.with(this@DetailActivity)
                .load(getString(R.string.baseUrlImage, movie.posterPath))
                .into(img_item_photo_detail)
            progressBar.visibility = View.GONE

            Glide.with(this@DetailActivity)
                .load(getString(R.string.baseUrlImage, movie.posterPath))
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(RequestOptions.bitmapTransform(BlurTransformation(5, 2)))
                .into(mbackdrop)
            mbackdrop.tag = movie.posterPath


            var favoriteState = movie.favorite
            setFavoriteButtonState(favoriteState)
            binding.detailContent.favouriteButton.setOnClickListener {
                favoriteState = !favoriteState
                if (favoriteState){
                    Snackbar.make(it, getString(R.string.add_to_fav),
                        Snackbar.LENGTH_SHORT).show()
                }else{
                    Snackbar.make(it, getString(R.string.removed_from_fav),
                        Snackbar.LENGTH_SHORT).show()
                }
                viewModel.setFavoriteMovie(movie, favoriteState)
                setFavoriteButtonState(favoriteState)
            }
        }
    }
    private fun setFavoriteButtonState(state: Boolean) {
        binding.detailContent.favouriteButton.isFavorite = state

    }

    companion object {
        const val EXTRA_MOVIE = "extraMovie"
    }

}