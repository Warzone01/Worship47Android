package com.kirdevelopment.worship47andorid2.home.adapters

import com.kirdevelopment.worship47andorid2.models.Result

interface SongClickListener {

    // нажатие на песню в списке
    fun onSongClicked(song: Result, position: Int){}
}