package com.kirdevelopment.worship47andorid2.leftMenu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kirdevelopment.worship47andorid2.databinding.FragmentLeftMenuBinding
import com.kirdevelopment.worship47andorid2.utils.AppState

class LeftMenuFragment(lmClicks: LmClicks) : Fragment() {

    private lateinit var binding: FragmentLeftMenuBinding
    private val clicks = lmClicks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            getArgs(it)
        }
        AppState.isLeftMenuOpen = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLeftMenuBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setClicks()
    }

    override fun onDestroy() {
        super.onDestroy()
        AppState.isLeftMenuOpen = false
    }

    // устанавливает клики на кнопки в левом меню
    private fun setClicks() {

        // нажатие на кнопку рандомной песни
        binding.llLmRandomBtn.setOnClickListener {
            clicks.clickRandomBtn()
        }

        // нажатие на кнопку выхода из аккаунта
        binding.llLmExitBtn.setOnClickListener {
            clicks.clickExitBtn()
        }

        // нажатие на пустую область
        binding.vPaddingLeftMenu.setOnClickListener {
            clicks.clickPadding()
        }
    }

    // получает аргументы при старте активити
    private fun getArgs(bundle: Bundle) {

    }
}