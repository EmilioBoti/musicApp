package com.example.musicapp.interactors

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.musicapp.R
import com.example.musicapp.interfaces.OnClickPlayerBar
import com.example.musicapp.interfaces.UpdateUI
import com.example.musicapp.model.MusicData
import com.example.musicapp.model.MusicDataNotParcelable
import com.example.musicapp.view.MainActivity
import com.google.gson.Gson


class PlayBackInput(val activity: Context, val updateUI: UpdateUI ): OnClickPlayerBar,
    MediaPlayer.OnCompletionListener {

    private var listMusic: ArrayList<MusicData> = ArrayList()
    private var media: MediaPlayer
    var songPlaying: Int = 0
    private var inicialPlay: Int = 0

    init {
        media = MediaPlayer()
        getSharePrefsAlbum()
    }

    override fun onClickBackBtn() {
        if (listMusic.isNotEmpty()){
            if(songPlaying > 0) songPlaying--
            inicialPlay = 0
            playSong(songPlaying, inicialPlay)
            updateUI.modifyBarPlayer(songPlaying, activity.getDrawable(R.drawable.pause_24), listMusic[songPlaying])
        }else
            Toast.makeText(activity, "Not Song to Play", Toast.LENGTH_SHORT).show()
    }

    override fun onClickPlayBtn() {
        if (listMusic.isNotEmpty()) playSong(songPlaying, inicialPlay)
        else Toast.makeText(activity, "Not Song to Play", Toast.LENGTH_SHORT).show()
    }

    override fun onClickNextBtn() {
        if (listMusic.isNotEmpty()) playNext()
        else Toast.makeText(activity, "Not Song to Play", Toast.LENGTH_SHORT).show()
    }

    override fun onClickSong(pos: Int, listSongs: ArrayList<MusicData>) {
        listMusic = listSongs
        songPlaying = pos
        inicialPlay = 0
        playSong(songPlaying, inicialPlay)
        saveCurrentSongPlaying(listMusic)
    }

    override fun onClickBanner(mainActivity: MainActivity) {
    }

    private fun toPlaySong(songToPlay: Int){
        if (this.media.isPlaying) this.media.reset()
        updateUI.modifyBarPlayer(songToPlay, activity.getDrawable(R.drawable.pause_24), listMusic[songPlaying])
        media = MediaPlayer.create(activity, listMusic.get(songToPlay).contentUri)
        updateUI.animMusicView("play")
        media.setOnCompletionListener(this)
        media.start()
        threadToUpdateProgress()
        inicialPlay = 1
    }
    private fun threadToUpdateProgress(){
        Thread {
            while (media.isPlaying) {
                updateUI.updateProgressBar(media.currentPosition, media.duration)
            }
        }.start()
    }
    private fun playSong(songToPlay: Int, play: Int){
        when (play){
            0 ->{
                toPlaySong(songToPlay)
            }
            1 ->{
                pauseSong()
                inicialPlay = 1
            }
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        playNext()
    }

    private fun playNext(){
        songPlaying++
        if (songPlaying >= listMusic.size ){
            songPlaying = 0
            this.media.reset()
            updateUI.modifyBarPlayer(null, activity.getDrawable(R.drawable.play_arrow_24), listMusic[songPlaying])
        }else{
            inicialPlay = 0
            playSong(songPlaying, inicialPlay)
            updateUI.modifyBarPlayer(songPlaying, activity.getDrawable(R.drawable.pause_24), listMusic[songPlaying])
        }

    }
    private fun pauseSong(){
        if (this.media.isPlaying){
            this.media.pause()
            updateUI.animMusicView("pause")
            updateUI.modifyBarPlayer(null, activity.getDrawable(R.drawable.play_arrow_24), listMusic[songPlaying])
        }else{
            updateUI.animMusicView("play")
            updateUI.modifyBarPlayer(null, activity.getDrawable(R.drawable.pause_24), listMusic[songPlaying])
            this.media.start()
            threadToUpdateProgress()
        }
    }

    private fun getSharePrefsAlbum() {

        val share = activity.getSharedPreferences(activity.getString(R.string.prefs_file), Context.MODE_PRIVATE)

        val song = share?.getString("song", null)
        val uris = share?.getString("uris", null)
        val currentPosition = share?.getInt("current", 0)
        songPlaying = share?.getInt("songPlaying", 0)!!


        val getGson = Gson()

        song?.let {
            val musicData = getGson.fromJson<ArrayList<MusicDataNotParcelable>>(it, ArrayList::class.java)
            val uri = getGson.fromJson<ArrayList<String>>(uris, ArrayList::class.java)

            for (item in uri.indices){
                val js = getGson.toJson(musicData[item])
                val music = getGson.fromJson(js, MusicDataNotParcelable::class.java)
                listMusic.add(MusicData(music.id, music.title, music.name, music.album, music.albumArtist,
                    music.mimeType, Uri.parse(uri[item])))
            }

            listMusic[songPlaying].title?.let { title ->
                listMusic[songPlaying].albumArtist?.let {  by ->
                    updateUI.currentSongPlaying(title,by)
                }
            }
        }
    }
    private fun saveCurrentSongPlaying(listSongs: ArrayList<MusicData>){

        val share = activity.getSharedPreferences(activity.getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val  ediot = share?.edit()

        val list: ArrayList<MusicDataNotParcelable> = arrayListOf()
        val listUri: ArrayList<String> = arrayListOf()

        listSongs.forEach {
            val music = MusicDataNotParcelable(
                it.id,
                it.title,
                it.name,it.album, it.albumArtist, it.mimeType, null
            )
            listUri.add(it.contentUri.toString())
            list.add(music)
        }

        val gson = Gson()
        val json = gson.toJson(list)
        val uris = gson.toJson(listUri)

        ediot?.apply {
            putString("song", json)
            putString("uris", uris)
            putInt("songPlaying",songPlaying)
            putInt("current", media.currentPosition)
            apply()
        }

    }
}