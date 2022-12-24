package vini.lop.io.marvelappstarter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import vini.lop.io.marvelappstarter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        navHostFragment = supportFragmentManager.findFragmentById(
            R.id.fragmentContainerView
        ) as NavHostFragment

        val navController = navHostFragment.navController

        binding.bottomNavigation.apply {
            setupWithNavController(navController)
            setOnNavigationItemReselectedListener { }
        }
    }
}