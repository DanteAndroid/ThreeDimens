package com.dante.threedimens

import android.os.Bundle
import android.os.Handler
import android.text.format.DateUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
import com.dante.threedimens.utils.SecretModeHelper
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
        initSecretMode()
        setupDrawer()
        checkUpdate()
    }

    private fun initSecretMode() {
        val headView = nav_view.getHeaderView(0).findViewById<View>(R.id.headView)
        SecretModeHelper.attachSecretModeClick(headView)
    }

    private fun checkUpdate() {
        appViewModel.check()
    }

    private fun setupDrawer() {
        controller = findNavController(R.id.navHostFragment)
        controller.graph.startDestination =
            if (SecretModeHelper.isSecretMode()) R.id.nav_wall else R.id.nav_gank

        appBarConfiguration = AppBarConfiguration(
//            setOf(R.id.nav_gank, R.id.nav_meizi, R.id.nav_wall, R.id.nav_favorite),
            nav_view.menu,
            drawer_layout
        )
        toolbar.setupWithNavController(controller, appBarConfiguration)
        nav_view.menu.clear()
        nav_view.inflateMenu(if (SecretModeHelper.isSecretMode()) R.menu.main_drawer else R.menu.safe_menu)
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
            R.id.nav_booru, R.id.nav_favorite, R.id.nav_sht -> {
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
