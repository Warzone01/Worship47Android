package com.kirdevelopment.worship47andorid2.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.kirdevelopment.worship47andorid2.R
import com.kirdevelopment.worship47andorid2.auth.AuthActivity
import com.kirdevelopment.worship47andorid2.auth.AuthViewModel
import com.kirdevelopment.worship47andorid2.database.SongsEntity
import com.kirdevelopment.worship47andorid2.databinding.ActivityHomeBinding
import com.kirdevelopment.worship47andorid2.home.adapters.MainSongListAdapter
import com.kirdevelopment.worship47andorid2.interactors.MainInteractor
import com.kirdevelopment.worship47andorid2.models.AuthKey
import com.kirdevelopment.worship47andorid2.models.Result
import com.kirdevelopment.worship47andorid2.utils.Constants.ALL_SONGS
import com.kirdevelopment.worship47andorid2.utils.Constants.APP_PREFERENCES
import com.kirdevelopment.worship47andorid2.utils.Constants.CHILD_SONGS
import com.kirdevelopment.worship47andorid2.utils.Constants.CHRISTMAS_SONG
import com.kirdevelopment.worship47andorid2.utils.Constants.EASTER_SONGS
import com.kirdevelopment.worship47andorid2.utils.Constants.MAIN_SONGS
import com.kirdevelopment.worship47andorid2.utils.Constants.TOKEN
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var list: List<SongsEntity> = emptyList()
    private var mainInteractor = MainInteractor()
    private var authModel = AuthViewModel()
    private var category = ALL_SONGS
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
        val intent: Intent = intent
        setClicks()
        if (category == ALL_SONGS) {
            if (mKey?.getString(TOKEN, "") != "") {
                getAllSongs(mKey?.getString(TOKEN, "") ?: "")
            } else {
                getAllSongs(intent.getStringExtra(TOKEN) ?: "")
            }
        } else {
            updateSongs()
        }
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
            setPaddingClick()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    binding.homeAppbar.isPopupOpen = false
                    visibility = View.GONE
                    binding.homeAppbar.setHeaderMarker()
                }

            // слушает клик в попапе по всем песням
            setAllSongsClick()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    category = ALL_SONGS
                    binding.homeAppbar.setHeader(resources.getString(R.string.all_songs))
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
                    binding.homeAppbar.setHeader(resources.getString(R.string.main_songs))
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
                    binding.homeAppbar.setHeader(resources.getString(R.string.christmas_songs))
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
                    binding.homeAppbar.setHeader(resources.getString(R.string.easter_songs))
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
                    binding.homeAppbar.setHeader(resources.getString(R.string.children_songs))
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
        }
    }

    // получает список песен
    private fun getAllSongs(token: String) {
        GlobalScope.launch(Dispatchers.IO) {
            songs = mainInteractor.getAllSongs(token = token)

            withContext(Dispatchers.Main) {
                adapter.addFirstSongs(songs)
                binding.homePreloader.visibility = View.GONE
                binding.rvMainSongList.visibility = View.VISIBLE
                binding.tvEmptyListStub.visibility = View.GONE
            }

            while (mainInteractor.page?.isNotBlank() == true || mainInteractor.page != null) {
                val nextSongs = mainInteractor.getNextSongs(token = token)
                songs.addAll(nextSongs)
                withContext(Dispatchers.Main) {
                    adapter.addFirstSongs(nextSongs)
                }
            }
        }
    }

    // обновляет и сортирует песни
    private fun updateSongs() {
        adapter.sortedSongs(category, songs)
    }
}