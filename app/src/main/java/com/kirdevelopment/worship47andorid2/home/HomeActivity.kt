package com.kirdevelopment.worship47andorid2.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.kirdevelopment.worship47andorid2.R
import com.kirdevelopment.worship47andorid2.auth.AuthActivity
import com.kirdevelopment.worship47andorid2.databinding.ActivityHomeBinding
import com.kirdevelopment.worship47andorid2.detailSong.DetailActivity
import com.kirdevelopment.worship47andorid2.home.adapters.MainSongListAdapter
import com.kirdevelopment.worship47andorid2.home.adapters.SongClickListener
import com.kirdevelopment.worship47andorid2.models.Result
import com.kirdevelopment.worship47andorid2.models.SongParams
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
import com.kirdevelopment.worship47andorid2.filterCore.SortSongs
import com.kirdevelopment.worship47andorid2.leftMenu.LeftMenuFragment
import com.kirdevelopment.worship47andorid2.leftMenu.LmClicks
import com.kirdevelopment.worship47andorid2.utils.AppState
import com.kirdevelopment.worship47andorid2.views.TopPopupView
import com.skydoves.balloon.*
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.random.Random

class HomeActivity : AppCompatActivity(), SongClickListener, KeyboardUtils, LmClicks {

    private lateinit var binding: ActivityHomeBinding
    private var model = HomeViewModel()
    private var category = ALL_SONGS
    private var headerText = ALL_SONGS
    private var mKey: SharedPreferences? = null
    private var songs = ArrayList<Result>()
    private lateinit var adapter: MainSongListAdapter
    private var songClickedPosition: Int = -1
    private var animationDuration = 200L

    private var fragment: Fragment? = null
    private val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

    var songParams: SongParams = SongParams()

    private lateinit var linearLayoutManager: LinearLayoutManager

    private val firstVisibleItemPosition: Int
        get() = linearLayoutManager.findFirstVisibleItemPosition()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (songs.isNotEmpty()) updateAllSongs()

        linearLayoutManager = LinearLayoutManager(this)
        adapter = MainSongListAdapter(songs, this)
        mKey = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        binding.rvMainSongList.layoutManager = linearLayoutManager
        binding.rvMainSongList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        if (fragment == null) {
            fragment = LeftMenuFragment(this)
            transaction
                .replace(R.id.main_fragment_container, fragment as LeftMenuFragment)
                .commit()
        }

        // если песен ещё нет, то запрашивает первые 10 песен
        if (songs.isEmpty()) {
            getFirstSongs()
            binding.homePreloader
        }

        setClicks()
        setScrollListener()

        // следит за списком песен и обновляет их
        model.songsList.observe(this, {
            it.let { result ->
                endLoading()
                if (SortSongs.checkIsFilterNotEmpty()) {
                    sortSongs()
                } else {
                    adapter.sortedSongs(category, result)
                }

            }
        })

        // изменить название в шапке
        binding.homeAppbar.changeMainBar(
            isHome = true,
            titleText = headerText
        )
        setSearch()
    }

    override fun onBackPressed() {

        // если открыт топ попап закрываем его
        if (binding.homeAppbar.isPopupOpen) {
            binding.topPopup.apply {
                closeTopPopup()
            }
            return
        }

        // если открыто левое меню
        if (AppState.isLeftMenuOpen) {
            closeLm()
            return
        }
        super.onBackPressed()
    }

    override fun onSongClicked(song: Result, position: Int) {
        super.onSongClicked(song, position)
        this@HomeActivity.binding.etSearch.hideKeyboard()
        songClickedPosition = position
        val intent = Intent(this@HomeActivity, DetailActivity::class.java)
        intent.putExtra(SONG_ID, song.id)
        intent.putExtra(SONG, model.parseSongsToDb(song))
        startActivity(intent)
    }

    override fun clickRandomBtn() {
        closeLm()

        // выбирает случайную песню
        kotlin.runCatching {
            val song =
                model.songsList.value?.get(Random.nextInt(0, model.songsList.value?.size ?: 0))
            val intent = Intent(this@HomeActivity, DetailActivity::class.java)
            intent.putExtra(SONG_ID, song?.id)
            intent.putExtra(SONG, song?.let { model.parseSongsToDb(it) })
            startActivity(intent)
        }.onFailure {
            Log.e("Ошибка рандома", it.localizedMessage)
        }
    }

    override fun clickExitBtn() {
        closeLm()

        // выход из аккаунта, к регистрации/авторизации
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

    override fun clickPadding() {
        super.clickPadding()
        closeLm()
    }

    // настраивает поиск по песням
    private fun setSearch() {

        // если текст поиска не пустой то при перезапуске активности не сбрасываем поиск
        if (binding.etSearch.text.toString() != "") {
            searchSong(binding.etSearch.text.toString())
        }

        // следит за обновлением текста в строке поиск
        RxTextView.afterTextChangeEvents(binding.etSearch)
            .debounce(500, TimeUnit.MILLISECONDS)
            .map { it.editable().toString() }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(scope())
            .subscribe {
                if (it.isNotEmpty()) {
                    searchSong(it)
                } else {
                    if (songParams == SongParams()) {
                        updateSongs()
                    }
                }

                if (!it.isNullOrBlank() && songs.isEmpty()) {
                    binding.tvEmptyListStub.visibility = View.VISIBLE
                } else {
                    binding.tvEmptyListStub.visibility = View.GONE
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
                    closeTopPopup()
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
                    sortSongs()
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
                    sortSongs()
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
                    sortSongs()
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
                    sortSongs()
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
                    sortSongs()
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
                        animatePopup(this@HomeActivity.binding.topPopup, !isPopupOpen)
                        setHeaderMarker()

                    } else {
                        isPopupOpen = false
                        animatePopup(this@HomeActivity.binding.topPopup, !isPopupOpen)
                        setHeaderMarker()
                    }
                }

            // показать окно сортировки
            setSortClick()
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {

                    // закрывает поиск и попап
                    isPopupOpen = false
                    animatePopup(this@HomeActivity.binding.topPopup, !isPopupOpen)
                    setHeaderMarker()

                    if (setFilterBalloon().isShowing) {
                        setFilterBalloon().dismiss()
                    } else {
                        setFilterBalloon().apply {
                            showAsDropDown(
                                anchor = this@HomeActivity.binding.homeAppbar.findViewById(
                                    R.id.iv_sort_songs
                                )
                            )
                            SortSongs.setClicksSortChords(
                                this,
                                songParams,
                                getDrawable(R.drawable.ic_arrow_up),
                                getDrawable(R.drawable.ic_arrow_down)
                            )
                            SortSongs.setClicksSortCategories(
                                this,
                                songParams,
                                getDrawable(R.drawable.ic_arrow_up),
                                getDrawable(R.drawable.ic_arrow_down)
                            )
                            SortSongs.setClicksSortLevel(
                                this,
                                songParams,
                                getDrawable(R.drawable.ic_arrow_up),
                                getDrawable(R.drawable.ic_arrow_down)
                            )
                            SortSongs.setClickSortTranslate(this, songParams)
                            SortSongs.setClearClick(this, songParams)
                            SortSongs.setApplyClick(this)
                        }
                    }
                }

            // устанавливает клик на кнопку поиска
            setLoginClicks()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    if (this@HomeActivity.binding.homeAppbar.isPopupOpen) {
                        closeTopPopup()
                    }
                    setSearchState(!isSearchMode)
                }

            // устанавливается клик на кнопку вызова левого меню
            setLmClicks()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300L, TimeUnit.MILLISECONDS)
                .autoDispose(scope()).subscribe {
                    this@HomeActivity.binding.etSearch.hideKeyboard()
                    AppState.isLeftMenuOpen = true
                    animateLm(this@HomeActivity.binding.mainFragmentContainer, false)
                }
        }
    }

    // слушатель изменения положения скролла
    private fun setScrollListener() {
        binding.rvMainSongList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
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

    // обновить песни
    private fun updateAllSongs() {
        if (mKey?.getString(TOKEN, "") != "") {
            model.updateSongs(mKey?.getString(TOKEN, "") ?: "", applicationContext)
        } else {
            model.updateSongs(intent.getStringExtra(TOKEN) ?: "", applicationContext)
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
        binding.tvWaitForDownloading.visibility = View.GONE
        binding.rvMainSongList.visibility = View.VISIBLE
        binding.tvEmptyListStub.visibility = View.GONE
    }

    // обновляет и сортирует песни
    private fun updateSongs() {
        if (binding.homeAppbar.isSearchMode) {
            searchSong(binding.etSearch.text.toString())
        } else {
            model.songsList.value?.let {
                adapter.sortSongsForParams(
                    it,
                    model.filterParms.value,
                    category
                )
            }
        }
    }

    // поиск по песням
    private fun searchSong(text: String) {
        model.songsList.value?.let {
            adapter.searchSong(
                text = text,
                songsForSort = it,
                params = model.filterParms.value,
                category = category
            )
        }
    }

    // сортировка по заданым параметрам
    private fun sortSongs() {
        model.filterParms.value = songParams
        if (binding.homeAppbar.isSearchMode) {
            searchSong(binding.etSearch.text.toString())
        } else {
            model.songsList.value?.let {
                adapter.sortSongsForParams(
                    it,
                    model.filterParms.value,
                    category
                )
            }
        }
        binding.rvMainSongList.scrollToPosition(0)
        if (songs.isNullOrEmpty()) {
            binding.tvEmptyListStub.visibility = View.VISIBLE
        } else {
            binding.tvEmptyListStub.visibility = View.GONE
        }
    }

    // показать сортировку
    private fun setFilterBalloon(): Balloon {
        return Balloon.Builder(this)
            .setLayout(R.layout.sort_layout)
            .setArrowSize(10)
            .setArrowAlignAnchorPadding(0)
            .setArrowOrientation(ArrowOrientation.TOP)
            .setWidthRatio(0.8f)
            .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
            .setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            .setCornerRadius(4f)
            .setOnBalloonDismissListener {
                sortSongs()
            }
            .setBalloonAnimation(BalloonAnimation.OVERSHOOT)
            .build()
    }

    // анимирует появление и исчезновение попапа
    private fun animatePopup(popup: TopPopupView, isHide: Boolean = false) {
        popup.apply {
            if (!isHide) {
                visibility = View.VISIBLE
                translationY = -1000f
                animate()
                    .translationY(1f)
                    .setDuration(animationDuration)
                    .interpolator = LinearInterpolator()
            } else {
                translationY = 1f
                animate()
                    .withEndAction { visibility = View.GONE }
                    .translationY(-1000f)
                    .setDuration(animationDuration)
                    .interpolator = LinearInterpolator()
            }
        }
    }

    // анимирует появление и счезновение поиска
    private fun animateSearch(search: LinearLayout, isHide: Boolean) {
        search.apply {
            if (!isHide) {
                visibility = View.VISIBLE
                translationY = -100f
                animate()
                    .translationY(1f)
                    .setDuration(animationDuration)
                    .interpolator = LinearInterpolator()
            } else {
                translationY = 1f
                animate()
                    .withEndAction { visibility = View.GONE }
                    .translationY(-100f)
                    .setDuration(animationDuration)
                    .interpolator = LinearInterpolator()
            }
        }
    }

    // устанавливает состояние левого меню
    private fun animateLm(fragment: FragmentContainerView, isHide: Boolean) {
        fragment.apply {
            if (!isHide) {
                closeTopPopup()
                visibility = View.VISIBLE
                translationX = -800f
                animate()
                    .translationX(0f)
                    .setDuration(animationDuration)
                    .interpolator = LinearInterpolator()
            } else {
                translationX = 0f
                animate()
                    .withEndAction { visibility = View.GONE }
                    .translationX(-800f)
                    .setDuration(animationDuration)
                    .interpolator = LinearInterpolator()
            }
        }
    }

    // закрывает верхний попап
    private fun closeTopPopup() {
        binding.topPopup.apply {
            binding.homeAppbar.isPopupOpen = false
            animatePopup(this@HomeActivity.binding.topPopup, true)
            binding.homeAppbar.setHeaderMarker()
        }
    }

    // закрывает левое меню
    private fun closeLm() {
        AppState.isLeftMenuOpen = false
        animateLm(binding.mainFragmentContainer, true)
    }

    // закрыть или открыть поиск
    private fun setSearchState(isNeedActive: Boolean, isNeedUpdateSongs: Boolean = true) {
        if (isNeedActive) {
            animateSearch(binding.searchLayout, false)
            binding.etSearch.requestFocus()
            binding.homeAppbar.showKeyboard()
            binding.homeAppbar.isSearchMode = true
            binding.homeAppbar.setSearchIcon()
        } else {
            animateSearch(binding.searchLayout, true)
            binding.etSearch.text = "" as? Editable
            if (isNeedUpdateSongs) updateSongs()

            binding.homeAppbar.hideKeyboard()
            binding.homeAppbar.isSearchMode = false
            binding.homeAppbar.setSearchIcon()
        }
    }
}