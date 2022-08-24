package com.kirdevelopment.worship47andorid2.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.kirdevelopment.worship47andorid2.R
import com.kirdevelopment.worship47andorid2.databinding.TabElementBinding

class TabItemView(context: Context, attrs: AttributeSet? = null): LinearLayout(context, attrs) {

    private var binding: TabElementBinding

    init {
        binding = TabElementBinding.inflate(LayoutInflater.from(context))
        inflate(context, R.layout.tab_element, this)
    }

    // установить фокус на элемент
    fun setFocused() {
        findViewById<TextView>(R.id.tv_tab).setTextColor(context.getColor(R.color.main_orange))
        findViewById<ImageView>(R.id.iv_underline_tab).setBackgroundResource(R.drawable.focused_under_line)
    }

    // снять фокус с элемента
    fun setUnfocused() {
        findViewById<TextView>(R.id.tv_tab).setTextColor(context.getColor(R.color.black_light))
        findViewById<ImageView>(R.id.iv_underline_tab).setBackgroundResource(R.drawable.under_line)
    }

    // установить текст
    fun setText(text: String) {
        findViewById<TextView>(R.id.tv_tab).text = text
    }
}