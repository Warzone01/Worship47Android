package com.kirdevelopment.worship47andorid2.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.kirdevelopment.worship47andorid2.ClickListeners
import com.kirdevelopment.worship47andorid2.R
import com.kirdevelopment.worship47andorid2.databinding.ButtonDownloadLayoutBinding

class ChordsDownloadButtonsView(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    private var binding: ButtonDownloadLayoutBinding

    init {
        binding = ButtonDownloadLayoutBinding.inflate(LayoutInflater.from(context))
        inflate(context, R.layout.button_download_layout, this)
    }

    // устанавливает всё необходимое для кнопок скачивания аккордов
    fun setButtons(linksList: ArrayList<String>, chordsList:ArrayList<String>, clickListeners: ClickListeners) {

        val downloadButton1 = findViewById<TextView>(R.id.tv_button_download_chord1)
        val downloadButton2 = findViewById<TextView>(R.id.tv_button_download_chord2)
        val downloadButton3 = findViewById<TextView>(R.id.tv_button_download_chord3)

        downloadButton1.setOnClickListener {
            clickListeners.downloadChordClickListener(0)
        }

        downloadButton2.setOnClickListener {
            clickListeners.downloadChordClickListener(1)
        }

        downloadButton3.setOnClickListener {
            clickListeners.downloadChordClickListener(2)
        }

        when (linksList.size) {
            1 -> {
                if (linksList[0].isNotBlank()) downloadButton1.visibility = View.VISIBLE
                downloadButton2.visibility = View.GONE
                downloadButton3.visibility = View.GONE

                setButtonText(downloadButton1, chordsList[0])
            }

            2 -> {
                if (linksList[0].isNotBlank()) downloadButton1.visibility = View.VISIBLE
                if (linksList[1].isNotBlank()) downloadButton2.visibility = View.VISIBLE
                downloadButton3.visibility = View.GONE

                setButtonText(downloadButton1, chordsList[0])
                setButtonText(downloadButton2, chordsList[1])
            }

            3 -> {
                if (linksList[0].isNotBlank()) downloadButton1.visibility = View.VISIBLE
                if (linksList[1].isNotBlank()) downloadButton2.visibility = View.VISIBLE
                if (linksList[2].isNotBlank()) downloadButton3.visibility = View.VISIBLE

                setButtonText(downloadButton1, chordsList[0])
                setButtonText(downloadButton2, chordsList[1])
                setButtonText(downloadButton3, chordsList[2])
            }

            else -> {
                downloadButton1.visibility = View.GONE
                downloadButton2.visibility = View.GONE
                downloadButton3.visibility = View.GONE
            }
        }
    }

    // устанавливает текст в кнопку
    private fun setButtonText(textView: TextView, chordName: String) {
        textView.text = resources.getString(R.string.download_chords, chordName)
    }
}