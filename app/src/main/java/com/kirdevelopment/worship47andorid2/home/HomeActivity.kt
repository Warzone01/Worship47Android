package com.kirdevelopment.worship47andorid2.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.kirdevelopment.worship47andorid2.R
import com.kirdevelopment.worship47andorid2.database.SongsEntity
import com.kirdevelopment.worship47andorid2.databinding.ActivityHomeBinding
import com.kirdevelopment.worship47andorid2.home.adapters.MainSongListAdapter
import com.kirdevelopment.worship47andorid2.views.AppBarView
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var list: List<SongsEntity> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvMainSongList.layoutManager = LinearLayoutManager(this)
        binding.rvMainSongList.adapter = MainSongListAdapter(list)
    }

    override fun onResume() {
        super.onResume()
        setClicks()
        showStub()
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

            setAllSongsClick()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    binding.homeAppbar.setHeader(resources.getString(R.string.all_songs))
                }

            setMainSongsClick()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    binding.homeAppbar.setHeader(resources.getString(R.string.main_songs))
                }

            setChristmasSongsClick()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    binding.homeAppbar.setHeader(resources.getString(R.string.christmas_songs))
                }

            setEasterSongsClick()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    binding.homeAppbar.setHeader(resources.getString(R.string.easter_songs))
                }

            setChildSongsClick()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    binding.homeAppbar.setHeader(resources.getString(R.string.children_songs))
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
        }
    }

    // показывает заглушку при необходимости
    private fun showStub() {
        if (list.isNullOrEmpty()) {
            binding.rvMainSongList.visibility = View.GONE
            binding.tvEmptyListStub.visibility = View.VISIBLE
        } else {
            binding.rvMainSongList.visibility = View.VISIBLE
            binding.tvEmptyListStub.visibility = View.GONE
        }
    }
}