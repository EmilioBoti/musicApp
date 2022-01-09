package com.example.musicapp.interfaces

import com.example.musicapp.model.MusicData
import com.example.musicapp.view.MainActivity

interface OnClickPlayerBar {
    fun onClickBackBtn()
    fun onClickPlayBtn()
    fun onClickNextBtn()
    fun onClickSong(pos: Int, listSongs: ArrayList<MusicData>)
    fun onClickBanner(mainActivity: MainActivity)
}