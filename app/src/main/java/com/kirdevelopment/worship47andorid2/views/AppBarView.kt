package com.kirdevelopment.worship47andorid2.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.kirdevelopment.worship47andorid2.R
import com.kirdevelopment.worship47andorid2.databinding.MainBarBinding
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable

class AppBarView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ConstraintLayout(context, attrs) {

    var binding: MainBarBinding
    var isPopupOpen: Boolean = false

    init {
        binding = MainBarBinding.inflate(LayoutInflater.from(context))
        inflate(context, R.layout.main_bar, this)
    }

    // установка слушателя нажатий на кноку меню
    fun setMenuClicks(): Observable<Unit> {
        return findViewById<LinearLayout>(R.id.ll_appbar_header).clicks()
    }

    fun setHeader(text: String) {
        findViewById<TextView>(R.id.tv_appbar_header_text).text = text
    }

    fun setHeaderMarker() {
        if (isPopupOpen) {
            findViewById<ImageView>(R.id.iv_appbar_arrow).setBackgroundResource(R.drawable.ic_arrow_up)
        } else {
            findViewById<ImageView>(R.id.iv_appbar_arrow).setBackgroundResource(R.drawable.ic_arrow_down)
        }
    }
}