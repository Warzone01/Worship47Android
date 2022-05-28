package com.kirdevelopment.worship47andorid2.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kirdevelopment.worship47andorid2.R
import com.kirdevelopment.worship47andorid2.auth.AuthActivity
import com.kirdevelopment.worship47andorid2.database.SongsEntity
import com.kirdevelopment.worship47andorid2.databinding.ActivityHomeBinding
import com.kirdevelopment.worship47andorid2.detailSong.DetailActivity
import com.kirdevelopment.worship47andorid2.home.adapters.MainSongListAdapter
import com.kirdevelopment.worship47andorid2.interactors.MainInteractor
import com.kirdevelopment.worship47andorid2.models.Result
import com.kirdevelopment.worship47andorid2.utils.Constants.ALL_SONGS
import com.kirdevelopment.worship47andorid2.utils.Constants.APP_PREFERENCES
import com.kirdevelopment.worship47andorid2.utils.Constants.CHILD_SONGS
import com.kirdevelopment.worship47andorid2.utils.Constants.CHRISTMAS_SONG
import com.kirdevelopment.worship47andorid2.utils.Constants.EASTER_SONGS
import com.kirdevelopment.worship47andorid2.utils.Constants.MAIN_SONGS
import com.kirdevelopment.worship47andorid2.utils.Constants.SONGS_NAME
import com.kirdevelopment.worship47andorid2.utils.Constants.SONGS_SUBTITLE
import com.kirdevelopment.worship47andorid2.utils.Constants.SONGS_TEXT
import com.kirdevelopment.worship47andorid2.utils.Constants.TOKEN
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.subjects.CompletableSubject
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.coroutines.coroutineContext

class HomeActivity : AppCompatActivity(), Observer<ArrayList<Result>> {

    private lateinit var binding: ActivityHomeBinding
    private var list: List<SongsEntity> = emptyList()
    private var mainInteractor = MainInteractor()
    private var model = HomeViewModel()
    private var category = ALL_SONGS
    private var headerText = "Все песни"
    private var mKey: SharedPreferences? = null
    private var songs = ArrayList<Result>()
    private lateinit var adapter: MainSongListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MainSongListAdapter(songs)
        mKey = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        binding.rvMainSongList.layoutManager = LinearLayoutManager(this)
        binding.rvMainSongList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        if (songs.isEmpty()) {
            getFirstSongs()
        }
        setClicks()

        model.songsList.observe(this, {
            it.let {
                getNextSongs()
                Log.d("Песни", "слушатель вызвался")
                adapter.addFirstSongs(it)
            }
        })
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

    // устанавливает клики
    private fun setClicks() {
        binding.topPopup.apply {

            // слушает клик вне попапа
            setPaddingClick()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    binding.homeAppbar.setHeaderMarker()
                }

            // слушает клик в попапе по всем песням
            setAllSongsClick()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    category = ALL_SONGS
                    headerText = resources.getString(R.string.all_songs)
                    binding.homeAppbar.setHeader(headerText)
                    binding.homeAppbar.isPopupOpen = false
                    visibility = View.GONE
                    binding.homeAppbar.setHeaderMarker()
                    updateSongs()
                }

            // слушает клик в попапе по основным песням
            setMainSongsClick()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    category = MAIN_SONGS
                    headerText = resources.getString(R.string.main_songs)
                    binding.homeAppbar.setHeader(headerText)
                    binding.homeAppbar.isPopupOpen = false
                    visibility = View.GONE
                    binding.homeAppbar.setHeaderMarker()
                    updateSongs()
                }

            // слушает клик в попапе по рождественским песням
            setChristmasSongsClick()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    category = CHRISTMAS_SONG
                    headerText = resources.getString(R.string.christmas_songs)
                    binding.homeAppbar.setHeader(headerText)
                    binding.homeAppbar.isPopupOpen = false
                    visibility = View.GONE
                    binding.homeAppbar.setHeaderMarker()
                    updateSongs()
                }

            // слушает клик в попапе по пасхальным песням
            setEasterSongsClick()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    category = EASTER_SONGS
                    headerText = resources.getString(R.string.easter_songs)
                    binding.homeAppbar.setHeader(headerText)
                    binding.homeAppbar.isPopupOpen = false
                    visibility = View.GONE
                    binding.homeAppbar.setHeaderMarker()
                    updateSongs()
                }

            // слушает клик в попапе по детским песням
            setChildSongsClick()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    category = CHILD_SONGS
                    headerText = resources.getString(R.string.children_songs)
                    binding.homeAppbar.setHeader(headerText)
                    binding.homeAppbar.isPopupOpen = false
                    visibility = View.GONE
                    binding.homeAppbar.setHeaderMarker()
                    updateSongs()
                }
        }

        binding.homeAppbar.apply {
            setMenuClicks()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    if (!isPopupOpen) {
                        isPopupOpen = true
                        this@HomeActivity.binding.topPopup.visibility = View.VISIBLE
                        setHeaderMarker()
                    } else {
                        isPopupOpen = false
                        this@HomeActivity.binding.topPopup.visibility = View.GONE
                        setHeaderMarker()
                    }
                }

            // устанавливает клик на кнопку выйти
            setLoginClicks()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    if (mKey?.getString(TOKEN, "") != "") {
                        val editor = mKey?.edit()
                        editor?.remove(TOKEN)
                        editor?.clear()
                        editor?.apply()
                    }
                    startActivity(
                        Intent(this@HomeActivity, AuthActivity::class.java)
                    )
                    finish()
                }

            // устанавливает клики на кнопку меню
            setLmClicks()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    val intent = Intent(this@HomeActivity, DetailActivity::class.java)
                    intent.putExtra(SONGS_NAME, model.songsList.value?.firstOrNull()?.title)
                    intent.putExtra(SONGS_SUBTITLE, model.songsList.value?.firstOrNull()?.title_eng)
                    intent.putExtra(SONGS_TEXT, model.songsList.value?.firstOrNull()?.text)
                    startActivity(intent)
                }
        }
    }

    // получить первые 10 песен
    private fun getFirstSongs(){
        if (mKey?.getString(TOKEN, "") != "") {
            model.getFirstSongs(mKey?.getString(TOKEN, "") ?: "")
            endLoading()
        } else {
            model.getFirstSongs(intent.getStringExtra(TOKEN) ?: "")
            endLoading()
        }
    }

    // получает пагинацию песен
    private fun getNextSongs() {
        val intent: Intent = intent

        // если мы залогинены получает песни по токену из префов, если нет, то по токену из ответа
        if (category == ALL_SONGS) {
            if (mKey?.getString(TOKEN, "") != "") {
                model.getNextSongs(mKey?.getString(TOKEN, "") ?: "")
            } else {
                model.getNextSongs(intent.getStringExtra(TOKEN) ?: "")
            }
        } else {
            updateSongs()
        }
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

    override fun onChanged(t: ArrayList<Result>?) {

    }
}