package com.hafidrf.dominictoretto.feature

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.hafidrf.dominictoretto.R
import kotlinx.android.synthetic.main.app_toolbar.view.*

class AppToolbar : Toolbar {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        View.inflate(context, R.layout.app_toolbar, this)
    }

    fun onNavBackPressed(onClickListener: OnClickListener) {
        toolbarNavIcon.setOnClickListener(onClickListener)
    }
}

fun AppToolbar.appToolbarTitle(title: String?) {
    if (!title.isNullOrEmpty())
        toolbarTitle.text = title
}

fun AppToolbar.appToolbarActionTitle(title: String?) {
    if (!title.isNullOrEmpty()) {
    }
}