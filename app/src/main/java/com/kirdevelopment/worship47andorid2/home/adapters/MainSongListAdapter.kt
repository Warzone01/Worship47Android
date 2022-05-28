package com.kirdevelopment.worship47andorid2.home.adapters

import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kirdevelopment.worship47andorid2.R
import com.kirdevelopment.worship47andorid2.database.SongsEntity
import com.kirdevelopment.worship47andorid2.models.Result
import androidx.lifecycle.ViewModel
import com.kirdevelopment.worship47andorid2.utils.Constants.ALL_SONGS

class MainSongListAdapter(private val songs: ArrayList<Result>) :
    RecyclerView.Adapter<MainSongListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.song_item,
                parent,
                false
            )
        )
    }

    // сортировка песен
    fun sortedSongs(category: String, songsForSort: ArrayList<Result>) {
        songs.clear()
        songs.addAll(if (category != ALL_SONGS) {
            songsForSort.filter {
                it.category.map { element -> element.title }.contains(category)
            }.sortedBy { it.title }
        } else {
            songsForSort.sortedBy { it.title }
        })
        notifyDataSetChanged()
    }

    // добавление первых песен в список
    fun addFirstSongs(songsList: ArrayList<Result>) {
        songs.addAll(songsList)
        Log.d("Песни", songs.size.toString())
        notifyItemRangeInserted(itemCount, songsList.size)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val expandButton = holder.expandButton

        // устанавливаем информацию для конкретного элемента
        holder.bindSongs(songItem = songs[position])

        // слушает нажатие на кнопку свернуть/развернуть
        expandButton.setOnClickListener {
            if (!songs[position].songExpanded) {
                holder.expandElement()
                songs[position].songExpanded = true
            } else {
                holder.rollUpElement()
                songs[position].songExpanded = false
            }
        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    // холдер элементов списка
    class ViewHolder(items: View) : RecyclerView.ViewHolder(items) {

        private val songNameRus = items.findViewById<TextView>(R.id.tv_song_item_ru_name)
        private val songNameEng = items.findViewById<TextView>(R.id.tv_song_item_eng_name)
        private val songChord = items.findViewById<TextView>(R.id.tv_main_chord_expanded)
        private val songChordNotExpanded =
            items.findViewById<TextView>(R.id.tv_main_chord_not_expanded)
        private val songShortText = items.findViewById<TextView>(R.id.tv_song_short_text)
        private val songAuthorName = items.findViewById<TextView>(R.id.tv_author_name_element)
        private val dividerNotExpanded = items.findViewById<ImageView>(R.id.divider_not_expanded)
        private val dividerExpanded = items.findViewById<ImageView>(R.id.divider_expanded)
        private val expandedArrowDown = items.findViewById<ImageView>(R.id.iv_expand_arrow_down)
        private val expandedArrowUp = items.findViewById<ImageView>(R.id.iv_expand_arrow_up)
        val expandButton = items.findViewById<FrameLayout>(R.id.iv_expand_element)

        // расставляет нужную информацию по элементу
        fun bindSongs(songItem: Result) {
            songNameRus.text = Html.fromHtml(songItem.title)
            songNameEng.text = Html.fromHtml(songItem.title_eng)
            songChord.text = Html.fromHtml(songItem.main_key)
            songChordNotExpanded.text = Html.fromHtml(songItem.main_key)
            songShortText.text = Html.fromHtml(songItem.text)
            songAuthorName.text = Html.fromHtml(songItem.author)
        }

        // развернуть элемент
        fun expandElement() {
            songChordNotExpanded.visibility = View.GONE
            dividerNotExpanded.visibility = View.GONE
            songChord.visibility = View.VISIBLE
            songAuthorName.visibility = View.VISIBLE
            songShortText.visibility = View.VISIBLE
            dividerExpanded.visibility = View.VISIBLE
            expandedArrowDown.visibility = View.GONE
            expandedArrowUp.visibility = View.VISIBLE
        }

        // свенуть элемент
        fun rollUpElement() {
            songChordNotExpanded.visibility = View.VISIBLE
            dividerNotExpanded.visibility = View.VISIBLE
            songChord.visibility = View.GONE
            songAuthorName.visibility = View.GONE
            songShortText.visibility = View.GONE
            dividerExpanded.visibility = View.GONE
            expandedArrowDown.visibility = View.VISIBLE
            expandedArrowUp.visibility = View.GONE
        }
    }
}