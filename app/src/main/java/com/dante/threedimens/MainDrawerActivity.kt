package com.dante.threedimens

import android.os.Bundle
import android.os.Handler
import android.text.format.DateUtils
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.dante.threedimens.ui.main.AppViewModel
import com.dante.threedimens.utils.InjectorUtils
import com.dante.threedimens.utils.Share
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main_drawer.*
import kotlinx.android.synthetic.main.app_bar_main_drawer.*
import org.jetbrains.anko.design.snackbar

class MainDrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var backPressed: Boolean = false
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var controller: NavController
    private val appViewModel: AppViewModel by viewModels {
        InjectorUtils.provideAppViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_drawer)
        setSupportActionBar(toolbar)
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        setupDrawer()
        checkUpdate()
    }

    private fun checkUpdate() {
        appViewModel.check()
    }

    private fun setupDrawer() {
        controller = findNavController(R.id.navHostFragment)
        appBarConfiguration = AppBarConfiguration(
//            setOf(R.id.nav_main, R.id.nav_meizi, R.id.nav_wall, R.id.nav_favorite),
            nav_view.menu,
            drawer_layout
        )
        toolbar.setupWithNavController(controller, appBarConfiguration)
        nav_view.setupWithNavController(controller)
        nav_view.setNavigationItemSelectedListener(this)
    }


    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            doublePressBackToQuit()
        }
    }

    private fun doublePressBackToQuit() {
        if (backPressed) {
            super.onBackPressed()
            return
        }
        backPressed = true
        nav_view.snackbar(R.string.leave_app)
        Handler().postDelayed({ backPressed = false }, 2 * DateUtils.SECOND_IN_MILLIS)
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
            R.id.nav_booru, R.id.nav_favorite -> {
                nav_view.snackbar(R.string.column_not_available)
            }
            R.id.nav_share -> {
                Share.shareText(this, getString(R.string.share_app_description))
            }
            R.id.nav_setting -> {
                nav_view.snackbar(R.string.column_not_available)
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
