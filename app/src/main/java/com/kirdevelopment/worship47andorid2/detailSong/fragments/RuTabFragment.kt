package com.kirdevelopment.worship47andorid2.detailSong.fragments

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kirdevelopment.worship47andorid2.database.SongsEntity
import com.kirdevelopment.worship47andorid2.databinding.FragmentRuTabBinding
import com.kirdevelopment.worship47andorid2.detailSong.DetailViewModel

class RuTabFragment(private var song: SongsEntity) : Fragment() {

    private lateinit var binding: FragmentRuTabBinding
    private var model = DetailViewModel()

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
        binding.tvRuText.text = Html.fromHtml(song.songTextRu)
    }
}