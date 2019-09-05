package com.example.base.base

import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.bugtags.library.Bugtags

/**
 * @author Dante
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

    override fun onResume() {
        super.onResume()
        Bugtags.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        Bugtags.onPause(this)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Bugtags.onDispatchTouchEvent(this, ev)
        return super.dispatchTouchEvent(ev)
    }

    abstract fun enableBack(): Boolean
    abstract fun initView()

    open fun initData() {
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