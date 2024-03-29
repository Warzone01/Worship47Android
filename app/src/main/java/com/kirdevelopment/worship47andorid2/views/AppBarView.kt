package com.kirdevelopment.worship47andorid2.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
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
    var isSearchMode: Boolean = false

    init {
        binding = MainBarBinding.inflate(LayoutInflater.from(context))
        inflate(context, R.layout.main_bar, this)
    }

    // установка слушателя нажатий на кноку меню
    fun setMenuClicks(): Observable<Unit> {
        return findViewById<LinearLayout>(R.id.ll_appbar_header).clicks()
    }

    // установить клики на кнопку меню
    fun setLmClicks(): Observable<Unit> {
        return findViewById<ImageView>(R.id.iv_menu).clicks()
    }

    // установка слушателя нажатий на кноку меню
    fun setLoginClicks(): Observable<Unit> {
        return findViewById<LinearLayout>(R.id.iv_info).clicks()
    }

    fun setSortClick(): Observable<Unit> {
        return findViewById<ImageView>(R.id.iv_sort_songs).clicks()
    }

    // установить заголовок
    fun setHeader(text: String) {
        findViewById<TextView>(R.id.tv_appbar_header_text).text = text
    }

    // установить иконку поиска
    fun setSearchIcon() {

        if (isSearchMode) {
            findViewById<ImageView>(R.id.iv_info).setImageResource(R.drawable.ic_search_off)
        } else {
            findViewById<ImageView>(R.id.iv_info).setImageResource(R.drawable.ic_search)
        }
    }

    // установить маркер для заголовка
    fun setHeaderMarker() {
        if (isPopupOpen) {
            findViewById<ImageView>(R.id.iv_appbar_arrow).setBackgroundResource(R.drawable.ic_arrow_up)
        } else {
            findViewById<ImageView>(R.id.iv_appbar_arrow).setBackgroundResource(R.drawable.ic_arrow_down)
        }
    }

    fun changeMainBar(
        isHome: Boolean = true,
        titleText: String,
        subtitleText: String = ""
    ) {
        val marker = findViewById<ImageView>(R.id.iv_appbar_arrow)
        val title = findViewById<TextView>(R.id.tv_appbar_header_text)
        val subtitle = findViewById<TextView>(R.id.tv_appbar_subtitle_text)
        val rightButton = findViewById<ImageView>(R.id.iv_info)
        val leftButton = findViewById<ImageView>(R.id.iv_menu)
        val filterButton = findViewById<ImageView>(R.id.iv_sort_songs)

        if (isHome) {
            marker.visibility = View.VISIBLE
            leftButton.visibility = View.VISIBLE
            filterButton.visibility = View.VISIBLE
            leftButton.setBackgroundResource(R.drawable.ic_menu)
            setSearchIcon()
            title.text = titleText
            subtitle.visibility = View.GONE
            setHeaderMarker()
        } else {
            marker.visibility = View.GONE
            leftButton.visibility = View.VISIBLE
            subtitle.visibility = View.VISIBLE
            rightButton.visibility = View.VISIBLE
            filterButton.visibility = View.GONE
            leftButton.setImageResource(R.drawable.ic_back)
            rightButton.setImageResource(R.drawable.ic_info)
            title.text = titleText
            subtitle.text = subtitleText
        }
    }
}