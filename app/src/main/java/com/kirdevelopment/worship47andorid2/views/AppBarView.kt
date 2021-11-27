package com.kirdevelopment.worship47andorid2.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.kirdevelopment.worship47andorid2.R
import com.kirdevelopment.worship47andorid2.databinding.MainBarBinding

class AppBarView(context: Context, attrs: AttributeSet? = null): ConstraintLayout(context, attrs) {

    private lateinit var binding: MainBarBinding

    init {
        binding = MainBarBinding.inflate(LayoutInflater.from(context))
        inflate(context, R.layout.main_bar, this)
        setClicks()
    }

    private fun setClicks() {

        binding.ivMenu.setOnClickListener {

        }

        binding.llAppbarHeader.setOnClickListener {
            Toast.makeText(context, "Отклик есть", Toast.LENGTH_SHORT).show()
            binding.ivAppbarArrow.setImageResource(R.drawable.ic_arrow_up)
        }

        binding.ivInfo.setOnClickListener {

        }

    }

}