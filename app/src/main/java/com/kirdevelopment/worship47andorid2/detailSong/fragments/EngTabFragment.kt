package com.kirdevelopment.worship47andorid2.detailSong.fragments

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kirdevelopment.worship47andorid2.database.SongsEntity
import com.kirdevelopment.worship47andorid2.databinding.FragmentEngTabBinding
import com.kirdevelopment.worship47andorid2.detailSong.DetailViewModel


class EngTabFragment(private var song: SongsEntity) : Fragment() {

    private lateinit var binding: FragmentEngTabBinding
    private val model = DetailViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEngTabBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.tvEngText.text = Html.fromHtml(song.songTextEng)
    }
}