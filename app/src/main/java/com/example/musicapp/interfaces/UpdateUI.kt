package com.example.musicapp.interfaces

import android.graphics.drawable.Drawable
import com.example.musicapp.model.MusicData

interface UpdateUI {
    fun modifyBarPlayer(pos: Int?, drawable: Drawable?, musicData: MusicData)
    fun animMusicView(toPlay: String)
    fun currentSongPlaying(title: String, by: String)
    fun updateProgressBar(current: Int, duration: Int)
}