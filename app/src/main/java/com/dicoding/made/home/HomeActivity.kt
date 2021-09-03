package com.dicoding.made.home

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.dicoding.made.R
import com.dicoding.made.databinding.ActivityHomeBinding
import com.dicoding.made.movies.MoviesFragment
import com.dicoding.made.tvshows.TvShowsFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigationChange(MoviesFragment())

        binding.bottomNavigationContainer.setNavigationChangeListener { _, position ->
            when (position) {
                0 -> navigationChange(MoviesFragment())
                1 -> navigationChange(TvShowsFragment())
                2 -> moveToFavoriteFragment()
            }
        }
    }

    private val className: String
        get() = "com.dicoding.made.favorite.FavoriteFragment"

    private fun moveToFavoriteFragment() {
        val fragment = instantiateFragment(className)
        if (fragment != null) {
            navigationChange(fragment)
        }
    }

    private fun instantiateFragment(className: String): Fragment? {
        return try {
            Class.forName(className).newInstance() as Fragment
        } catch (e: Exception) {
            Toast.makeText(this, "Module not found", Toast.LENGTH_SHORT).show()
            null
        }
    }

    private fun navigationChange(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameContainer, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }
}