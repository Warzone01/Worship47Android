package com.kirdevelopment.worship47andorid2.detailSong

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.kirdevelopment.worship47andorid2.R
import com.kirdevelopment.worship47andorid2.databinding.ActivityDetailBinding
import com.kirdevelopment.worship47andorid2.models.Result
import com.kirdevelopment.worship47andorid2.utils.Constants.SONGS_NAME
import com.kirdevelopment.worship47andorid2.utils.Constants.SONGS_SUBTITLE
import com.kirdevelopment.worship47andorid2.utils.Constants.SONGS_TEXT
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import java.util.concurrent.TimeUnit

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTabsText()
    }

    override fun onResume() {
        super.onResume()
        initTabs()
        val songsTitle = intent.getStringExtra(SONGS_NAME)
        val songsSubtitle = intent.getStringExtra(SONGS_SUBTITLE)
        val songsText = intent.getStringExtra(SONGS_TEXT)
        binding.homeAppbar.changeMainBar(isHome = false, songsTitle ?: "", songsSubtitle ?: "")
        binding.homeAppbar.setLmClicks()
            .throttleFirst(300, TimeUnit.MILLISECONDS)
            .autoDispose(scope()).subscribe {
                finish()
            }
    }

    // создаёт вкладки
    private fun initTabs() {
        binding.vpSongText.adapter = DetailSongTabsAdapter(supportFragmentManager, lifecycle)
        binding.vpSongText.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                when (position) {
                    0 -> {
                        binding.tabRu.setFocused()
                        binding.tabEng.setUnfocused()
                        binding.tabChord.setUnfocused()

                        binding.tabEng.setOnClickListener {
                            binding.vpSongText.currentItem = 1
                        }

                        binding.tabChord.setOnClickListener {
                            binding.vpSongText.currentItem = 2
                        }
                    }

                    1 -> {
                        binding.tabRu.setUnfocused()
                        binding.tabEng.setFocused()
                        binding.tabChord.setUnfocused()

                        binding.tabRu.setOnClickListener {
                            binding.vpSongText.currentItem = 0
                        }

                        binding.tabChord.setOnClickListener {
                            binding.vpSongText.currentItem = 2
                        }
                    }

                    else -> {
                        binding.tabRu.setUnfocused()
                        binding.tabEng.setUnfocused()
                        binding.tabChord.setFocused()

                        binding.tabRu.setOnClickListener {
                            binding.vpSongText.currentItem = 0
                        }

                        binding.tabEng.setOnClickListener {
                            binding.vpSongText.currentItem = 1
                        }
                    }
                }

            }
        })
    }

    // устанавливает текст для вкладок
    private fun setTabsText() {
        binding.tabRu.setText(getString(R.string.ru_text))
        binding.tabEng.setText(getString(R.string.eng_text))
        binding.tabChord.setText(getString(R.string.chord_text))
    }
}