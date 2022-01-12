package com.example.musicapp.interactors

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.musicapp.R
import com.example.musicapp.interfaces.OnClickPlayerBar
import com.example.musicapp.interfaces.UpdateUI
import com.example.musicapp.model.MusicData
import com.example.musicapp.model.MusicDataNotParcelable
import com.example.musicapp.view.MainActivity
import com.google.gson.Gson
import java.io.File

class PlayBackInput(val activity: Context, val updateUI: UpdateUI ): OnClickPlayerBar,
    MediaPlayer.OnCompletionListener {

    private var listMusic: ArrayList<MusicData> = ArrayList()
    private var media: MediaPlayer
    private var songPlaying: Int = 0
    private var inicialPlay: Int = 0
    private var threadProcess: Thread? = null
    val channelId = "com.example.musicapp"
    val notificationId = 1

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

    override fun onTouchProgressbar(process: Int) {
        if(!listMusic.isEmpty()){
            media.stop()
            toPlaySong(songPlaying, process)
        }
    }

    private fun toPlaySong(songToPlay: Int, seekPos: Int){
        if (this.media.isPlaying) this.media.reset()

        updateUI.modifyBarPlayer(songToPlay, activity.getDrawable(R.drawable.pause_24), listMusic[songPlaying])
        media = MediaPlayer.create(activity, listMusic.get(songToPlay).contentUri)
        updateUI.animMusicView("play")
        media.setOnCompletionListener(this)
        media.seekTo(seekPos)
        media.start()
        //threadToUpdateProgress()
        updateUI.updateProgressBar(media.currentPosition, media.duration)
        inicialPlay = 1

    }
    private fun threadToUpdateProgress() {
        //threadProcess?.interrupt()
        threadProcess = Thread {
            while (media.isPlaying) {
                updateUI.updateProgressBar(media.currentPosition, media.duration)
            }
        }
        threadProcess?.start()

    }
    private fun playSong(songToPlay: Int, play: Int){
        when (play){
            0 ->{
                toPlaySong(songToPlay, 0)
                showNotifycationMusic()
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
           // threadToUpdateProgress()
        }
    }

    //notification in status bar
    private fun showNotifycationMusic(){

        val notificationBuider = NotificationCompat.Builder(activity, channelId)
            .setSmallIcon(R.drawable.music_note_24)
            .setContentTitle(listMusic[songPlaying].album)
            .setContentText(listMusic[songPlaying].title)
            .addAction(R.drawable.skip_previous_24, "Previous", null)
            .addAction(R.drawable.play_arrow_24, "Play", null)
            .addAction(R.drawable.skip_next_24, "Next", null)
            .setLargeIcon(null)
            //.setProgress(media.duration, 20000, false)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(activity)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, notificationBuider.build())
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