package com.kirdevelopment.worship47andorid2.filterCore

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.kirdevelopment.worship47andorid2.R
import com.kirdevelopment.worship47andorid2.models.SongParams
import com.kirdevelopment.worship47andorid2.utils.Constants
import com.skydoves.balloon.Balloon

/**
 * Настройка сортировки песен
 * Обрабатывает базовые функции, такие как нажатия, выделения, применение и сброс
 */
object SortSongs {

    // активны ли пункты выбора
    private var isMain = false
    private var isChristmas = false
    private var isEaster = false
    private var isChildren = false

    private var isEasy = false
    private var isMedium = false
    private var isHard = false

    private var isC = false
    private var isD = false
    private var isE = false
    private var isF = false
    private var isG = false
    private var isA = false
    private var isB = false

    private var isCheckedTranslator = false

    // устанавливает нажатия в сортировке по танальности
    fun setClicksSortChords(
        balloon: Balloon, songParams: SongParams, iconDrawableUp: Drawable?,
        iconDrawableDown: Drawable?
    ) {

        // основной контейнер
        val container =
            balloon.getContentView().findViewById<LinearLayout>(R.id.ll_chords_sort_expandable)
        val icon = balloon.getContentView().findViewById<ImageView>(R.id.iv_arrow_chord_sort)
        val chordsContainer =
            balloon.getContentView().findViewById<LinearLayout>(R.id.ll_chords_select)

        // пункты выбора
        val c = balloon.getContentView().findViewById<CheckBox>(R.id.cb_c)
        val d = balloon.getContentView().findViewById<CheckBox>(R.id.cb_d)
        val e = balloon.getContentView().findViewById<CheckBox>(R.id.cb_e)
        val f = balloon.getContentView().findViewById<CheckBox>(R.id.cb_f)
        val g = balloon.getContentView().findViewById<CheckBox>(R.id.cb_g)
        val a = balloon.getContentView().findViewById<CheckBox>(R.id.cb_a)
        val b = balloon.getContentView().findViewById<CheckBox>(R.id.cb_b)

        // устанавливает статусы для чекбокса
        c.isChecked = isC
        d.isChecked = isD
        e.isChecked = isE
        f.isChecked = isF
        g.isChecked = isG
        a.isChecked = isA
        b.isChecked = isB

        // нажатие на основной пункт в списке для раскрытия/скрытия
        container.setOnClickListener {
            setIconForSort(icon, chordsContainer, iconDrawableUp, iconDrawableDown)
        }

        // нажатие на пункты выбора
        c.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                isC = true
                songParams.chords.add(Constants.C_CHORD)
            } else {
                isC = false
                songParams.chords.remove(Constants.C_CHORD)
            }
        }
        d.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                isD = true
                songParams.chords.add(Constants.D_CHORD)
            } else {
                isD = false
                songParams.chords.remove(Constants.D_CHORD)
            }
        }
        e.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                isE = true
                songParams.chords.add(Constants.E_CHORD)
            } else {
                isE = false
                songParams.chords.remove(Constants.E_CHORD)
            }
        }
        f.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                isF = true
                songParams.chords.add(Constants.F_CHORD)
            } else {
                isF = false
                songParams.chords.remove(Constants.F_CHORD)
            }
        }
        g.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                isG = true
                songParams.chords.add(Constants.G_CHORD)
            } else {
                isG = false
                songParams.chords.remove(Constants.G_CHORD)
            }
        }
        a.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                isA = true
                songParams.chords.add(Constants.A_CHORD)
            } else {
                isA = false
                songParams.chords.remove(Constants.A_CHORD)
            }
        }
        b.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                isB = true
                songParams.chords.add(Constants.B_CHORD)
            } else {
                isB = false
                songParams.chords.remove(Constants.B_CHORD)
            }
        }
    }

    // устанавливает нажатия в сортировке по категориям
    fun setClicksSortCategories(
        balloon: Balloon, songParams: SongParams, iconDrawableUp: Drawable?,
        iconDrawableDown: Drawable?
    ) {

        // основной контейнер
        val container =
            balloon.getContentView().findViewById<LinearLayout>(R.id.ll_categories_sort_expandable)
        val icon = balloon.getContentView().findViewById<ImageView>(R.id.iv_arrow_categories_sort)
        val categContainer =
            balloon.getContentView().findViewById<LinearLayout>(R.id.ll_category_select)

        // пункты выбора
        val mainCateg = balloon.getContentView().findViewById<CheckBox>(R.id.cb_main)
        val christmasCateg = balloon.getContentView().findViewById<CheckBox>(R.id.cb_christmas)
        val easterCateg = balloon.getContentView().findViewById<CheckBox>(R.id.cb_easter)
        val childrenCateg = balloon.getContentView().findViewById<CheckBox>(R.id.cb_children)

        mainCateg.isChecked = isMain
        christmasCateg.isChecked = isChristmas
        easterCateg.isChecked = isEaster
        childrenCateg.isChecked = isChildren

        // нажатие на основной пункт в списке для раскрытия/скрытия
        container.setOnClickListener {
            setIconForSort(icon, categContainer, iconDrawableUp, iconDrawableDown)
        }

        // нажатие на пункты выбора
        mainCateg.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                isMain = true
                songParams.categories.add(Constants.MAIN_SONGS)
            } else {
                isMain = false
                songParams.categories.remove(Constants.MAIN_SONGS)
            }
        }
        christmasCateg.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                isChristmas = true
                songParams.categories.add(Constants.CHRISTMAS_SONG)
            } else {
                isChristmas = false
                songParams.categories.remove(Constants.CHRISTMAS_SONG)
            }
        }
        easterCateg.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                isEaster = true
                songParams.categories.add(Constants.EASTER_SONGS)
            } else {
                isEaster = false
                songParams.categories.remove(Constants.EASTER_SONGS)
            }
        }
        childrenCateg.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                isChildren = true
                songParams.categories.add(Constants.CHILD_SONGS)
            } else {
                isChildren = false
                songParams.categories.remove(Constants.CHILD_SONGS)
            }
        }
    }

    // устанавливает нажатия в сортировке по сложности
    fun setClicksSortLevel(
        balloon: Balloon, songParams: SongParams, iconDrawableUp: Drawable?,
        iconDrawableDown: Drawable?
    ) {

        // основной контейнер
        val container =
            balloon.getContentView().findViewById<LinearLayout>(R.id.ll_level_sort_expandable)
        val icon = balloon.getContentView().findViewById<ImageView>(R.id.iv_arrow_level_sort)
        val levelContainer =
            balloon.getContentView().findViewById<LinearLayout>(R.id.ll_level_select)

        // пункты выбора
        val easy = balloon.getContentView().findViewById<CheckBox>(R.id.cb_easy)
        val medium = balloon.getContentView().findViewById<CheckBox>(R.id.cb_medium)
        val hard = balloon.getContentView().findViewById<CheckBox>(R.id.cb_hard)

        // устанавливает статусы чекобкса
        easy.isChecked = isEasy
        medium.isChecked = isMedium
        hard.isChecked = isHard

        // нажатие на основной пункт в списке для раскрытия/скрытия
        container.setOnClickListener {
            setIconForSort(icon, levelContainer, iconDrawableUp, iconDrawableDown)
        }

        // нажатие на пункты выбора
        easy.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                isEasy = true
                songParams.level.add(Constants.EASY_LEVEL)
            } else {
                isEasy = false
                songParams.level.remove(Constants.EASY_LEVEL)
            }
        }

        medium.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                isMedium = true
                songParams.level.add(Constants.MEDIUM_LEVEL)
            } else {
                isMedium = false
                songParams.level.remove(Constants.MEDIUM_LEVEL)
            }
        }

        hard.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                isHard = true
                songParams.level.add(Constants.HARD_LEVEL)
            } else {
                isHard = false
                songParams.level.remove(Constants.HARD_LEVEL)
            }
        }
    }

    // устанавливает клики на чекбокс перевода
    fun setClickSortTranslate(balloon: Balloon, songParams: SongParams) {
        val isTranslated = balloon.getContentView().findViewById<CheckBox>(R.id.cb_translated)

        isTranslated.isChecked = isCheckedTranslator
        isTranslated.setOnCheckedChangeListener { compoundButton, b ->
            isCheckedTranslator = b
            songParams.isTranslated = b
        }
    }

    // устанавливает клик на конопку очистить
    fun setClearClick(balloon: Balloon, songParams: SongParams) {
        val clearButton = balloon.getContentView().findViewById<TextView>(R.id.tv_btn_clear_filter)

        clearButton.setOnClickListener {
            songParams.apply {
                isTranslated = false
                chords = ArrayList()
                categories = ArrayList()
                level = ArrayList()
            }
            clearBools()
            balloon.dismiss()
        }
    }

    // устанавливает нажатие на кнопку применить
    fun setApplyClick(balloon: Balloon) {
        val applyButton = balloon.getContentView().findViewById<TextView>(R.id.tv_btn_apply_filter)

        applyButton.setOnClickListener {
            balloon.dismiss()
        }
    }

    fun clearBools() {
        isMain = false
        isChristmas = false
        isEaster = false
        isChildren = false

        isEasy = false
        isMedium = false
        isHard = false

        isC = false
        isD = false
        isE = false
        isF = false
        isG = false
        isA = false
        isB = false

        isCheckedTranslator = false
    }

    // проверяет есть ли выбранные категории
    fun checkIsFilterNotEmpty(): Boolean {
        return isMain
                || isChristmas
                || isEaster
                || isChildren
                || isEasy
                || isMedium
                || isHard
                || isC
                || isD
                || isE
                || isF
                || isG
                || isA
                || isB
                || isCheckedTranslator
    }

    // установить иконку для сортировки
    private fun setIconForSort(
        icon: ImageView,
        container: LinearLayout,
        iconDrawableUp: Drawable?,
        iconDrawableDown: Drawable?
    ) {
        if (container.isVisible) {
            container.visibility = View.GONE
            icon.setImageDrawable(iconDrawableDown)
        } else {
            container.visibility = View.VISIBLE
            icon.setImageDrawable(iconDrawableUp)
        }
    }
}