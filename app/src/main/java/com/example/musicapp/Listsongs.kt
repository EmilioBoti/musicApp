package com.example.musicapp

import android.animation.ObjectAnimator
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.interfaces.OnClickItemListListenner
import com.google.android.material.snackbar.Snackbar

class Listsongs : AppCompatActivity(), OnClickItemListListenner,View.OnClickListener, MediaPlayer.OnCompletionListener {
    var listsongs: ArrayList<MusicData> = arrayListOf()
    lateinit var recyclerViewSong: RecyclerView
    lateinit var media: MediaPlayer
    lateinit var barPlay: LinearLayout
    lateinit var btnBack: ImageButton
    lateinit var btnPlay: ImageButton
    lateinit var btnNext: ImageButton
    lateinit var currentSong: TextView
    private var songPlaying: Int = 0
    private var isSounding: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listsongs)

        init()
        listsongs = intent.getParcelableArrayListExtra<MusicData>("list") as ArrayList<MusicData>

        val listmusicAdapter = ListMusicAdapter(this, listsongs, this)
        recyclerViewSong.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewSong.addItemDecoration(DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL))
        recyclerViewSong.adapter = listmusicAdapter

        media = MediaPlayer.create(applicationContext, listsongs.get(songPlaying).contentUri)
        media.setOnCompletionListener(this)

        btnBack.setOnClickListener(this)
        btnPlay.setOnClickListener(this)
        btnNext.setOnClickListener(this)

    }
    fun init(){
        recyclerViewSong = findViewById(R.id.songsContaienr)
        barPlay = findViewById(R.id.barPlay)
        btnBack = findViewById(R.id.btnBack)
        btnPlay = findViewById(R.id.btnPlay)
        btnNext = findViewById(R.id.btnNext)
        currentSong = findViewById(R.id.currentSong)
        media = MediaPlayer()
    }

    override fun onClickViewList(pos: Int) {
        barPlay.visibility = View.VISIBLE
        btnPlay.setImageDrawable(getDrawable(R.drawable.pause_24))
        songPlaying = pos

        if (media.isPlaying){
            media.stop()
            media.reset()
        }
        if(songPlaying <= listsongs.size) playSong(songPlaying)

    }
    override fun onCompletion(mp: MediaPlayer?) {
        if (songPlaying < listsongs.size) playSong(songPlaying++)
        //Toast.makeText(applicationContext,"It has reached the end", Toast.LENGTH_SHORT).show()
    }

    private fun playSong(songToPlay: Int){
        if (media.isPlaying){
            media.stop()
        }
        //media.setDataSource(applicationContext, listsongs.get(songPlaying).contentUri!!)
        media = MediaPlayer.create(applicationContext, listsongs.get(songPlaying).contentUri)
        btnPlay.setImageDrawable(getDrawable(R.drawable.pause_24))
        currentSong.setText(listsongs.get(songPlaying).title)
        //media.prepare()
        media.setOnCompletionListener(this)
        media.start()
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnBack ->{
                if(songPlaying > 0) songPlaying--
                playSong(songPlaying)
            }
            R.id.btnPlay->{
                if (media.isPlaying){
                    media.pause()
                    btnPlay.setImageDrawable(getDrawable(R.drawable.play_arrow_24))
                }else{
                    btnPlay.setImageDrawable(getDrawable(R.drawable.pause_24))
                    currentSong.setText(listsongs.get(songPlaying).title)
                    media.start()
                }
            }
            R.id.btnNext->{
                songPlaying++
                if (songPlaying >= listsongs.size ){
                    songPlaying--
                    playSong(songPlaying)
                }else{
                    playSong(songPlaying)
                }
            }
        }
    }

}