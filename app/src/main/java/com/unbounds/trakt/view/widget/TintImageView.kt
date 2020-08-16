package com.unbounds.trakt.view.widget

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * Created by maclir on 2015-11-21.
 */
class TintImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : AppCompatImageView(context, attrs, defStyle) {
    private val tint: ColorStateList? = null

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        if (tint != null && tint.isStateful) updateTintColor()
    }

    private fun updateTintColor() {
        val color = tint!!.getColorForState(drawableState, 0)
        setColorFilter(color)
    }
}
