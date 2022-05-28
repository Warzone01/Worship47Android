package com.kirdevelopment.worship47andorid2.detailSong.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kirdevelopment.worship47andorid2.R
import com.kirdevelopment.worship47andorid2.databinding.ActivityHomeBinding
import com.kirdevelopment.worship47andorid2.databinding.FragmentRuTabBinding
import com.kirdevelopment.worship47andorid2.home.HomeViewModel

class RuTabFragment : Fragment() {

    private var model = HomeViewModel()
    private lateinit var binding: FragmentRuTabBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRuTabBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.tvRuText.text = model.songsList.value?.firstOrNull()?.text ?: "Не повезло не фартануло"
    }
}