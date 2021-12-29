package com.example.musicapp.interfaces

import android.view.ViewGroup
import com.example.musicapp.model.MusicData

interface OnClickPlayerBar {
    fun onClickBackBtn()
    fun onClickPlayBtn()
    fun onClickNextBtn()
    fun onClickSong(pos: Int, listSongs: ArrayList<MusicData>)
}