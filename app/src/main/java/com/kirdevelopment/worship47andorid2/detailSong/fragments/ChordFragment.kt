package com.kirdevelopment.worship47andorid2.detailSong.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kirdevelopment.worship47andorid2.R
import com.kirdevelopment.worship47andorid2.databinding.FragmentChordBinding
import com.kirdevelopment.worship47andorid2.home.HomeViewModel

class ChordFragment : Fragment() {

    private lateinit var binding: FragmentChordBinding
    private val model = HomeViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentChordBinding.inflate(LayoutInflater.from(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chord, container, false)
    }

    override fun onResume() {
        super.onResume()
        Log.d("Вызвалось",model.songsList.value?.size.toString())
        binding.tvChordText.text = model.songsList.value?.firstOrNull()?.text.toString()
    }
}