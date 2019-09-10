package com.dante.threedimens.utils.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

/**
 * @author Dante
 * 2019-09-05
 */
class FlingRecyclerView :
    RecyclerView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun fling(velocityX: Int, velocityY: Int): Boolean {
        val newVelocityY = velocityY * 0.7
        return super.fling(velocityX, newVelocityY.toInt())
    }

}