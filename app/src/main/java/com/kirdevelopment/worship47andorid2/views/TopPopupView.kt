package com.kirdevelopment.worship47andorid2.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.jakewharton.rxbinding2.view.clicks
import com.kirdevelopment.worship47andorid2.R
import com.kirdevelopment.worship47andorid2.databinding.TopPopupMenuBinding
import com.uber.autodispose.android.scope
import com.uber.autodispose.autoDispose
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class TopPopupView(context: Context, attrs: AttributeSet? = null): LinearLayout(context, attrs) {

    private var binding: TopPopupMenuBinding

    init {
        binding = TopPopupMenuBinding.inflate(LayoutInflater.from(context))
        inflate(context, R.layout.top_popup_menu, this)
    }

    // установить клик на пространство под попапом
    fun setPaddingClick(): Observable<Unit> {
        return findViewById<View>(R.id.padding_under_popup).clicks()
    }

    // устанавливает клик на все песни
    fun setAllSongsClick(): Observable<Unit> {
        return findViewById<LinearLayout>(R.id.ll_item_all_songs).clicks()
            .throttleFirst(300L, TimeUnit.MILLISECONDS)
            .doOnNext {
                setSelected(0)
            }
    }

    // устанавливает клик на основные песни
    fun setMainSongsClick(): Observable<Unit> {
        return findViewById<LinearLayout>(R.id.ll_item_main_songs).clicks()
            .throttleFirst(300L, TimeUnit.MILLISECONDS)
            .doOnNext {
                setSelected(1)
            }
    }

    // устанавливает клик на рождественские песни
    fun setChristmasSongsClick(): Observable<Unit> {
        return findViewById<LinearLayout>(R.id.ll_item_christmas_songs).clicks()
            .throttleFirst(300L, TimeUnit.MILLISECONDS)
            .doOnNext {
                setSelected(2)
            }
    }

    // устанавливает клик на пасхальные песни
    fun setEasterSongsClick(): Observable<Unit> {
        return findViewById<LinearLayout>(R.id.ll_item_easter_songs).clicks()
            .throttleFirst(300L, TimeUnit.MILLISECONDS)
            .doOnNext {
                setSelected(3)
            }
    }

    // устанавливает клик на детские песни
    fun setChildSongsClick(): Observable<Unit> {
        return findViewById<LinearLayout>(R.id.ll_item_children_songs).clicks()
            .throttleFirst(300L, TimeUnit.MILLISECONDS)
            .doOnNext {
                setSelected(4)
            }
    }

    // устанавливает выбранный пункт
    private fun setSelected(selectedItem: Int) {
        when (selectedItem) {

            0 -> {
                findViewById<ImageView>(R.id.all_indicator).visibility = View.VISIBLE
                findViewById<ImageView>(R.id.main_indicator).visibility = View.GONE
                findViewById<ImageView>(R.id.christmas_indicator).visibility = View.GONE
                findViewById<ImageView>(R.id.easter_indicator).visibility = View.GONE
                findViewById<ImageView>(R.id.children_indicator).visibility = View.GONE
            }

            1 -> {
                findViewById<ImageView>(R.id.all_indicator).visibility = View.GONE
                findViewById<ImageView>(R.id.main_indicator).visibility = View.VISIBLE
                findViewById<ImageView>(R.id.christmas_indicator).visibility = View.GONE
                findViewById<ImageView>(R.id.easter_indicator).visibility = View.GONE
                findViewById<ImageView>(R.id.children_indicator).visibility = View.GONE
            }

            2 -> {
                findViewById<ImageView>(R.id.all_indicator).visibility = View.GONE
                findViewById<ImageView>(R.id.main_indicator).visibility = View.GONE
                findViewById<ImageView>(R.id.christmas_indicator).visibility = View.VISIBLE
                findViewById<ImageView>(R.id.easter_indicator).visibility = View.GONE
                findViewById<ImageView>(R.id.children_indicator).visibility = View.GONE
            }

            3 -> {
                findViewById<ImageView>(R.id.all_indicator).visibility = View.GONE
                findViewById<ImageView>(R.id.main_indicator).visibility = View.GONE
                findViewById<ImageView>(R.id.christmas_indicator).visibility = View.GONE
                findViewById<ImageView>(R.id.easter_indicator).visibility = View.VISIBLE
                findViewById<ImageView>(R.id.children_indicator).visibility = View.GONE
            }

            else -> {
                findViewById<ImageView>(R.id.all_indicator).visibility = View.GONE
                findViewById<ImageView>(R.id.main_indicator).visibility = View.GONE
                findViewById<ImageView>(R.id.christmas_indicator).visibility = View.GONE
                findViewById<ImageView>(R.id.easter_indicator).visibility = View.GONE
                findViewById<ImageView>(R.id.children_indicator).visibility = View.VISIBLE
            }
        }
    }

}