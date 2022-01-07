package com.kirdevelopment.worship47andorid2.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kirdevelopment.worship47andorid2.R
import com.kirdevelopment.worship47andorid2.database.SongsEntity

class MainSongListAdapter(private val songs: List<SongsEntity>):
    RecyclerView.Adapter<MainSongListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.song_item,
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val expandButton = holder.expandButton

        // устанавливаем информацию для конкретного элемента
        holder.bindSongs(songItem = songs[position])

        // слушает нажатие на кнопку свернуть/развернуть
        expandButton.setOnClickListener {
            if (!songs[position].songExpanded) {
                holder.expandElement()
            } else {
                holder.rollUpElement()
            }
        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    // холдер элементов списка
    class ViewHolder(items: View): RecyclerView.ViewHolder(items) {

        private val songNameRus = items.findViewById<TextView>(R.id.tv_song_item_ru_name)
        private val songNameEng = items.findViewById<TextView>(R.id.tv_song_item_eng_name)
        private val songChord = items.findViewById<TextView>(R.id.tv_main_chord_expanded)
        private val songChordNotExpanded = items.findViewById<TextView>(R.id.tv_main_chord_not_expanded)
        private val songShortText = items.findViewById<TextView>(R.id.tv_song_short_text)
        private val songAuthorName = items.findViewById<TextView>(R.id.tv_author_name_element)
        private val dividerNotExpanded = items.findViewById<View>(R.id.divider_not_expanded)
        private val dividerExpanded = items.findViewById<View>(R.id.divider_expanded)
        val expandButton = items.findViewById<View>(R.id.iv_expand_element)

        // расставляет нужную информацию по элементу
        fun bindSongs(songItem: SongsEntity) {
            songNameRus.text = songItem.songNameRu
            songNameEng.text = songItem.songTextEng
            songChord.text = songItem.songChords[0].toString()
            songChordNotExpanded.text = songItem.songChords[0].toString()
            songShortText.text = songItem.songTextRu
            songAuthorName.text = songItem.author
        }

        // развернуть элемент
        fun expandElement() {
            songChordNotExpanded.visibility = View.GONE
            dividerNotExpanded.visibility = View.GONE
            songChord.visibility = View.VISIBLE
            songAuthorName.visibility = View.VISIBLE
            songShortText.visibility = View.VISIBLE
            dividerExpanded.visibility = View.VISIBLE
        }

        // свенуть элемент
        fun rollUpElement() {
            songChordNotExpanded.visibility = View.VISIBLE
            dividerNotExpanded.visibility = View.VISIBLE
            songChord.visibility = View.GONE
            songAuthorName.visibility = View.GONE
            songShortText.visibility = View.GONE
            dividerExpanded.visibility = View.GONE
        }
    }
}