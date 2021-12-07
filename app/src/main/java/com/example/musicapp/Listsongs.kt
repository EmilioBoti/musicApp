package com.example.musicapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Listsongs : AppCompatActivity(), ListMusicAdapter.OnMusicClickListener {
    var listsongs: ArrayList<MusicData> = arrayListOf()
    lateinit var recyclerViewSong: RecyclerView
    lateinit var media: MediaPlayer
    lateinit var barPlay: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listsongs)

        listsongs = intent.getParcelableArrayListExtra<MusicData>("list") as ArrayList<MusicData>
        recyclerViewSong = findViewById(R.id.songsContaienr)
        barPlay = findViewById(R.id.barPlay)

        val listmusicAdapter = ListMusicAdapter(this, listsongs, this)
        recyclerViewSong.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewSong.adapter = listmusicAdapter

    }

    override fun onclickMusic(pos: Int) {
        barPlay.visibility = View.VISIBLE
        Toast.makeText(applicationContext, "${pos}", Toast.LENGTH_SHORT).show()
        media = MediaPlayer.create(applicationContext, listsongs.get(pos).contentUri)
        media.start()
    }
}