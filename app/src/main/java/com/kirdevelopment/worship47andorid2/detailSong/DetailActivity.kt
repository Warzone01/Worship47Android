package com.kirdevelopment.worship47andorid2.detailSong

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.widget.ViewPager2
import com.kirdevelopment.worship47andorid2.R
import com.kirdevelopment.worship47andorid2.database.SongsEntity
import com.kirdevelopment.worship47andorid2.database.StringConverter
import com.kirdevelopment.worship47andorid2.databinding.ActivityDetailBinding
import com.kirdevelopment.worship47andorid2.detailSong.fragments.SongInfoFragment
import com.kirdevelopment.worship47andorid2.utils.Constants.SONG
import com.kirdevelopment.worship47andorid2.utils.Constants.SONG_ID
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose

import java.util.concurrent.TimeUnit

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val model = DetailViewModel()
    var song: SongsEntity? = null
    private val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
    private var fragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTabsText()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        model.songId.value = intent.getIntExtra(SONG_ID, 0)
        song = intent.getSerializableExtra(SONG) as SongsEntity
    }

    override fun onResume() {
        model.getSong(this)
        super.onResume()

        if(fragment == null) {
            fragment = SongInfoFragment.newInstance(
                category = StringConverter.fromStringToList(song?.songCategorySlug ?: "")
                    .toMutableList() as ArrayList<String>,
                author = song?.author,
                presentation = song?.presentation,
                chords = StringConverter.fromStringToList(song?.chordsFilesLinks ?: "")
                    .toMutableList() as ArrayList<String>,
                videos = StringConverter.fromStringToList(song?.songVideoIds ?: "")
                    .toMutableList() as ArrayList<String>,
                translator = StringConverter.fromStringToList(song?.songTranslators ?: "")
                    .toMutableList() as ArrayList<String>,
                keys = StringConverter.fromStringToList(song?.songKeys ?: "")
                    .toMutableList() as ArrayList<String>
            )
            transaction
                .replace(R.id.song_info_fragment, fragment as SongInfoFragment)
                .commit()
        }

        binding.homeAppbar.changeMainBar(
            isHome = false,
            titleText = song?.songNameRu ?: "",
            subtitleText = song?.songNameEng ?: ""
        )
        binding.homeAppbar.setLmClicks()
            .throttleFirst(300, TimeUnit.MILLISECONDS)
            .autoDispose(scope()).subscribe {
                finish()
            }
        binding.homeAppbar.setLoginClicks()
            .throttleFirst(300, TimeUnit.MILLISECONDS)
            .autoDispose(scope()).subscribe {
                binding.songInfoFragment.visibility = View.VISIBLE
            }

        initTabs()
    }

    override fun onBackPressed() {
        if (binding.songInfoFragment.isVisible) {
            binding.songInfoFragment.visibility = View.GONE
            return
        }

        super.onBackPressed()
    }

    // создаёт вкладки
    private fun initTabs() {
        binding.vpSongText.adapter = DetailSongTabsAdapter(
            fm = supportFragmentManager,
            lifecycle = lifecycle,
            song = song ?: SongsEntity()
        )
        binding.vpSongText.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
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