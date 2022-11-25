package com.kirdevelopment.worship47andorid2.detailSong

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kirdevelopment.worship47andorid2.database.SongsEntity
import com.kirdevelopment.worship47andorid2.detailSong.fragments.ChordFragment
import com.kirdevelopment.worship47andorid2.detailSong.fragments.EngTabFragment
import com.kirdevelopment.worship47andorid2.detailSong.fragments.RuTabFragment

class DetailSongTabsAdapter(fm: FragmentManager, lifecycle: Lifecycle, val song: SongsEntity) :
    FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int {
        return when {
            isTwoTabs() -> 2
            isOneTab() -> 1
            else -> 3
        }
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {

            0 -> {
                when {
                    song.songTextRu.isNotBlank() -> RuTabFragment(song)
                    song.songTextEng.isNotBlank() && song.songTextRu.isBlank() -> EngTabFragment(
                        song
                    )
                    else -> ChordFragment(song)
                }

            }

            1 -> {
                when {
                    song.songTextEng.isNotBlank() && song.songTextRu.isBlank() && song.songChords.isNotBlank() -> ChordFragment(
                        song
                    )
                    song.songTextEng.isNotBlank() -> EngTabFragment(song)
                    else -> ChordFragment(song)
                }
            }

            else -> ChordFragment(song)

        }
    }

    // если текст одной из вкладок пустой
    private fun isTwoTabs(): Boolean {
        return song.songTextRu.isNotBlank()
                && song.songTextEng.isNotBlank()
                && song.songChords.isBlank() ||
                song.songTextRu.isNotBlank()
                && song.songTextEng.isBlank()
                && song.songChords.isNotBlank() ||
                song.songTextRu.isBlank()
                && song.songTextEng.isNotBlank()
                && song.songChords.isNotBlank()
    }

    // текст 2-х вкладок пустой
    private fun isOneTab(): Boolean {
        return song.songTextRu.isNotBlank()
                && song.songTextEng.isBlank()
                && song.songChords.isBlank() ||
                song.songTextRu.isBlank()
                && song.songTextEng.isBlank()
                && song.songChords.isNotBlank() ||
                song.songTextRu.isBlank()
                && song.songTextEng.isNotBlank()
                && song.songChords.isBlank()
    }

    // если все 3 вкладки заполнены
    private fun isThreeTabs(): Boolean {
        return song.songTextRu.isNotBlank()
                && song.songTextEng.isNotBlank()
                && song.songChords.isNotBlank()
    }
}