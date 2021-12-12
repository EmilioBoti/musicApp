package com.example.musicapp.interfaces

import android.media.MediaPlayer
import com.example.musicapp.MusicData

interface OnClickPlayerBar {
    fun onClickBackBtn(songPlaying: Int)
    fun onClickPlayBtn(songPlaying: Int, media: MediaPlayer)
    fun onClickNextBtn()
    fun onClickSong(pos: Int, musicData: MusicData)
}