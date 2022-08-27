package com.kirdevelopment.worship47andorid2.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.kirdevelopment.worship47andorid2.R
import com.kirdevelopment.worship47andorid2.databinding.ActivityHomeBinding
import com.kirdevelopment.worship47andorid2.detailSong.DetailActivity
import com.kirdevelopment.worship47andorid2.home.adapters.MainSongListAdapter
import com.kirdevelopment.worship47andorid2.home.adapters.SongClickListener
import com.kirdevelopment.worship47andorid2.models.Result
import com.kirdevelopment.worship47andorid2.utils.Constants.ALL_SONGS
import com.kirdevelopment.worship47andorid2.utils.Constants.APP_PREFERENCES
import com.kirdevelopment.worship47andorid2.utils.Constants.CHILD_SONGS
import com.kirdevelopment.worship47andorid2.utils.Constants.CHRISTMAS_SONG
import com.kirdevelopment.worship47andorid2.utils.Constants.EASTER_SONGS
import com.kirdevelopment.worship47andorid2.utils.Constants.MAIN_SONGS
import com.kirdevelopment.worship47andorid2.utils.Constants.SONG
import com.kirdevelopment.worship47andorid2.utils.Constants.SONG_ID
import com.kirdevelopment.worship47andorid2.utils.Constants.TOKEN
import com.kirdevelopment.worship47andorid2.utils.KeyboardUtils
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity(), SongClickListener, KeyboardUtils {

    private lateinit var binding: ActivityHomeBinding
    private var model = HomeViewModel()
    private var category = ALL_SONGS
    private var headerText = ALL_SONGS
    private var mKey: SharedPreferences? = null
    private var songs = ArrayList<Result>()
    private lateinit var adapter: MainSongListAdapter
    private var songClickedPosition: Int = -1

    private lateinit var linearLayoutManager: LinearLayoutManager

    private val firstVisibleItemPosition: Int
        get() = linearLayoutManager.findFirstVisibleItemPosition()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        linearLayoutManager = LinearLayoutManager(this)
        adapter = MainSongListAdapter(songs, this)
        mKey = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        binding.rvMainSongList.layoutManager = linearLayoutManager
        binding.rvMainSongList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        // если песен ещё нет, то запрашивает первые 10 песен
        if (songs.isEmpty()) {
            getFirstSongs()
            binding.homePreloader
        }

        setClicks()
        setSearch()
        setScrollListener()

        // следит за списком песен
        model.songsList.observe(this, {
            it.let { result ->
                endLoading()
                adapter.sortedSongs(category, result)
            }
        })

        // изменить название в шапке
        binding.homeAppbar.changeMainBar(
            isHome = true,
            titleText = headerText
        )
    }

    override fun onBackPressed() {

        // если открыт топ попап закрываем его
        if (binding.homeAppbar.isPopupOpen) {
            binding.topPopup.apply {
                binding.homeAppbar.isPopupOpen = false
                visibility = View.GONE
                binding.homeAppbar.setHeaderMarker()
            }
            return
        }
        super.onBackPressed()
    }

    override fun onSongClicked(song: Result, position: Int) {
        super.onSongClicked(song, position)
        songClickedPosition = position
        val intent = Intent(this@HomeActivity, DetailActivity::class.java)
        intent.putExtra(SONG_ID, song.id)
        intent.putExtra(SONG, model.parseSongsToDb(song))
        startActivity(intent)
    }

    // настраивает поиск по песням
    private fun setSearch() {

        // если текст поиска не пустой то при перезапуске активности не сбрасываем поиск
        if (binding.etSearch.text.toString() != "") {
            searchSong(binding.etSearch.text.toString())
        }

        // следит за обновлением текста в строке поиска
        binding.etSearch.doOnTextChanged { text, _, _, count ->
            if (count >= 1) {
                searchSong(text.toString())
            } else {
                updateSongs()
            }
        }
    }

    // устанавливает клики
    private fun setClicks() {
        binding.topPopup.apply {

            // слушает клик вне попапа
            setPaddingClick()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    visibility = View.GONE
                    binding.homeAppbar.isPopupOpen = false
                    binding.homeAppbar.setHeaderMarker()
                }

            // слушает клик в попапе по всем песням
            setAllSongsClick()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    setHeader(
                        category = ALL_SONGS,
                        headerText = resources.getString(R.string.all_songs)
                    )
                    visibility = View.GONE
                    updateSongs()
                    binding.rvMainSongList.scrollToPosition(0)
                }

            // слушает клик в попапе по основным песням
            setMainSongsClick()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    setHeader(
                        category = MAIN_SONGS,
                        headerText = resources.getString(R.string.main_songs)
                    )
                    visibility = View.GONE
                    updateSongs()
                    binding.rvMainSongList.scrollToPosition(0)
                }

            // слушает клик в попапе по рождественским песням
            setChristmasSongsClick()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    setHeader(
                        category = CHRISTMAS_SONG,
                        headerText = resources.getString(R.string.christmas_songs)
                    )
                    visibility = View.GONE
                    updateSongs()
                    binding.rvMainSongList.scrollToPosition(0)
                }

            // слушает клик в попапе по пасхальным песням
            setEasterSongsClick()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    setHeader(
                        category = EASTER_SONGS,
                        headerText = resources.getString(R.string.easter_songs)
                    )
                    visibility = View.GONE
                    updateSongs()
                    binding.rvMainSongList.scrollToPosition(0)
                }

            // слушает клик в попапе по детским песням
            setChildSongsClick()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    setHeader(
                        category = CHILD_SONGS,
                        headerText = resources.getString(R.string.children_songs)
                    )
                    visibility = View.GONE
                    updateSongs()
                    binding.rvMainSongList.scrollToPosition(0)
                }
        }

        // устанавливает клик на кнопку наверх
        binding.btnUp.setOnClickListener {
            binding.rvMainSongList.scrollToPosition(0)
            binding.btnUp.visibility = View.GONE
        }

        // устанавливает клик на шапку
        binding.homeAppbar.apply {
            setMenuClicks()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    if (!isPopupOpen) {
                        isPopupOpen = true
                        this@HomeActivity.binding.topPopup.visibility = View.VISIBLE
                        setHeaderMarker()

                        // закрывает поиск
                        setSearchState(false)
                    } else {
                        isPopupOpen = false
                        this@HomeActivity.binding.topPopup.visibility = View.GONE
                        setHeaderMarker()
                    }
                }

            // устанавливает клик на кнопку поиска
            setLoginClicks()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    if (this@HomeActivity.binding.homeAppbar.isPopupOpen) {
                        this@HomeActivity.binding.topPopup.apply {
                            this@HomeActivity.binding.homeAppbar.isPopupOpen = false
                            visibility = View.GONE
                            this@HomeActivity.binding.homeAppbar.setHeaderMarker()
                        }
                    }
                    setSearchState(!isSearchMode)
                }
        }
//  этот код, чтобы можно было выйти, просто сохранён до лучших времён
//        if (mKey?.getString(TOKEN, "") != "") {
//            val editor = mKey?.edit()
//            editor?.remove(TOKEN)
//            editor?.clear()
//            editor?.apply()
//        }
//        startActivity(
//            Intent(this@HomeActivity, AuthActivity::class.java)
//        )
//        finish()
    }

    // слушатель изменения положения скролла
    private fun setScrollListener() {
        binding.rvMainSongList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView.layoutManager?.itemCount
                if (firstVisibleItemPosition != 0) {
                    binding.btnUp.visibility = View.VISIBLE
                } else {
                    binding.btnUp.visibility = View.GONE
                }
            }
        })
    }

    // получить песни
    private fun getFirstSongs() {
        if (mKey?.getString(TOKEN, "") != "") {
            model.getFirstSongs(mKey?.getString(TOKEN, "") ?: "", applicationContext)
        } else {
            model.getFirstSongs(intent.getStringExtra(TOKEN) ?: "", applicationContext)
        }
    }

    // установить текст в шапке
    private fun setHeader(category: String, headerText: String) {
        this.category = category
        this.headerText = headerText
        binding.homeAppbar.setHeader(this.headerText)
        binding.homeAppbar.isPopupOpen = false
        binding.homeAppbar.setHeaderMarker()
    }

    // выставляет параметры завершения загрузки
    private fun endLoading() {
        binding.homePreloader.visibility = View.GONE
        binding.rvMainSongList.visibility = View.VISIBLE
        binding.tvEmptyListStub.visibility = View.GONE
    }

    // обновляет и сортирует песни
    private fun updateSongs() {
        model.songsList.value?.let { adapter.sortedSongs(category, it) }
    }

    // поиск по песням
    private fun searchSong(text: String) {
        model.songsList.value?.let { adapter.searchSong(text, it) }
    }

    // закрыть или открыть поиск
    private fun setSearchState(isNeedActive: Boolean) {
        if (isNeedActive) {
            binding.searchLayout.visibility = View.VISIBLE
            binding.etSearch.requestFocus()
            binding.homeAppbar.showKeyboard()
            binding.homeAppbar.isSearchMode = true
            binding.homeAppbar.setSearchIcon()
        } else {
            binding.searchLayout.visibility = View.GONE
            binding.etSearch.text = "" as? Editable
            updateSongs()
            binding.homeAppbar.hideKeyboard()
            binding.homeAppbar.isSearchMode = false
            binding.homeAppbar.setSearchIcon()
        }
    }
}