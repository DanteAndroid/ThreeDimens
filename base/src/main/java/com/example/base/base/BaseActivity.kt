package com.example.base.base

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

/**
 * @author Du Wenyu
 * 2019-08-19
 */
abstract class BaseActivity : AppCompatActivity() {

    abstract val layoutResId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)
        initView()
        initData()
    }

    abstract fun enableBack(): Boolean
    abstract fun initView()

    open fun initData(){
        supportActionBar?.setDisplayHomeAsUpEnabled(enableBack())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}