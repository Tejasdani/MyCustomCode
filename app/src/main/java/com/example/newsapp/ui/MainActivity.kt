package com.example.newsapp.ui

import android.app.ActivityManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.newsapp.MainViewModel
import com.example.newsapp.R
import com.example.newsapp.core.BaseVMBindingActivity
import com.example.newsapp.ui.savednews.ui.SavedEventsFragment
import com.example.newsapp.ui.dashboard.ui.HomeFragment
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.selectnews.SelectTopicsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseVMBindingActivity<ActivityMainBinding, MainViewModel>(
    MainViewModel::class.java
) {

    lateinit var homeFragment: HomeFragment
    lateinit var savedFragment: SavedEventsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadFragment(HomeFragment.newInstance())

        homeFragment = HomeFragment()
        savedFragment = SavedEventsFragment()
        val am: ActivityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        println("dalvik.vm.heapgrowthlimit: " + am.memoryClass);
        println("dalvik.vm.heapsize: " + am.largeMemoryClass);

        /*  val navView: BottomNavigationView = binding.navView
          val navController = findNavController(R.id.nav_host_fragment_activity_main)
          binding.navView.setupWithNavController(navController)
          navView.setupWithNavController(navController)*/

        binding.navView.setOnItemSelectedListener { item ->
            var fragment: Fragment
            when (item.itemId) {
                R.id.navigation_home -> {
                    //  toolbar?.setTitle("Home")
                    // fragment = HomeFragment()
                    loadFragment(homeFragment)
                    true
                }
                R.id.navigation_dashboard -> {
                    // toolbar?.setTitle("Radio")
                    // fragment = SavedEventsFragment()
                    loadFragment(savedFragment)
                    true

                }
                R.id.navigation_notifications -> {
                    navActivity()
                    true

                }
                else -> false
            }
        }
    }

    private fun navActivity() {
        val intent = Intent(this@MainActivity, SelectTopicsActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun getPersistentView(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }


    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

}