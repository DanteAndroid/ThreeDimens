package com.example.threedimens

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main_drawer.*
import kotlinx.android.synthetic.main.app_bar_main_drawer.*
import org.jetbrains.anko.toast

class MainDrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var controller: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_drawer)
        setSupportActionBar(toolbar)
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        setupDrawer()
    }

    private fun setupDrawer() {
        controller = findNavController(R.id.navHostFragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_main, R.id.nav_meizi, R.id.nav_wall, R.id.nav_favorite),
            drawer_layout
        )
        toolbar.setupWithNavController(controller, appBarConfiguration)
        nav_view.setupWithNavController(controller)
//        nav_view.setNavigationItemSelectedListener(this)
    }


    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return item.onNavDestinationSelected(controller)
                || super.onOptionsItemSelected(item)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_main -> {
                toast("picturesTabsFragment")

            }
            R.id.nav_meizi -> {
                toast("postsTabsFragment")
            }
            R.id.nav_favorite -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_setting -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return item.onNavDestinationSelected(controller)
    }

    override fun onSupportNavigateUp(): Boolean {
        return controller.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}
