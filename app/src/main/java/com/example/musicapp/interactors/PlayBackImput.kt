package com.example.musicapp.interactors

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.widget.Toast
import com.example.musicapp.R
import com.example.musicapp.interfaces.OnClickPlayerBar
import com.example.musicapp.interfaces.UpdateUI
import com.example.musicapp.model.MusicData

class PlayBackImput(val activity: Context, val updateUI: UpdateUI ): OnClickPlayerBar, MediaPlayer.OnCompletionListener {
    private var listMusic: ArrayList<MusicData> = ArrayList()
    var media: MediaPlayer
    var songPlaying: Int = 0
    var inicialPlay: Int = 0

    init {
        media = MediaPlayer()
        listMusic = getSharePrefsAlbum()
    }

    fun addListOfMusic(listSongs: ArrayList<MusicData>){
        listMusic = listSongs;
    }

    override fun onClickBackBtn() {
        if (!listMusic.isEmpty()){
            if(songPlaying > 0) songPlaying--
            inicialPlay = 0
            playSong(songPlaying, inicialPlay)
            updateUI.modifyBarPlayer(songPlaying, activity.getDrawable(R.drawable.pause_24), listMusic[songPlaying])
        }else
            Toast.makeText(activity, "Not Song to Play", Toast.LENGTH_SHORT).show()
    }

    override fun onClickPlayBtn() {
        if (!listMusic.isEmpty() ) playSong(songPlaying, inicialPlay)
        else Toast.makeText(activity, "Not Song to Play", Toast.LENGTH_SHORT).show()
    }

    override fun onClickNextBtn() {
        if (!listMusic.isEmpty()) playNext()
        else Toast.makeText(activity, "Not Song to Play", Toast.LENGTH_SHORT).show()
    }

    override fun onClickSong(pos: Int, listSongs: ArrayList<MusicData>) {
        listMusic = listSongs
        songPlaying = pos
        inicialPlay = 0
        playSong(songPlaying, inicialPlay)
        saveCurrentSongPlaying(listMusic)
    }

    private fun playSong(songToPlay: Int, play: Int){
        when (play){
            0 ->{
                if (this.media.isPlaying) this.media.reset()
                updateUI.modifyBarPlayer(songToPlay, activity.getDrawable(R.drawable.pause_24), listMusic[songPlaying])
                media = MediaPlayer.create(activity, listMusic.get(songToPlay).contentUri)
                updateUI.animMusicView("play")
                media.setOnCompletionListener(this)
                media.start()
                inicialPlay = 1
            }
            1 ->{
                pauseSong()
                inicialPlay = 1
            }
        }

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
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        playNext()
    }

    private fun getSharePrefsAlbum(): ArrayList<MusicData> {

        val share = activity.getSharedPreferences(activity.getString(R.string.prefs_file),Context.MODE_PRIVATE)

        var songName = share?.getString("music", "--")
        val uri = share?.getString("uriSong", "")
        val by = share?.getString("by", "--")
        val songDuration = share?.getInt("duration", 0)

        val uriSong: Uri = Uri.parse(uri)

        listMusic.add(MusicData(null, songName, null, null, by, null, uriSong))

        songName?.let { title ->
            by?.let {  by ->
                updateUI.currentSongPlaying(title,by)
            }
        }

        return listMusic;
    }
    private fun saveCurrentSongPlaying(listSongs: ArrayList<MusicData>){

        val uriSong = listSongs[songPlaying].contentUri
        val nameSong= listSongs[songPlaying].name
        val by = listSongs[songPlaying].AlbumArtist
        val songDuration = media.duration

        val share = activity.getSharedPreferences(activity.getString(R.string.prefs_file)
            , Context.MODE_PRIVATE)
        val  ediot = share?.edit()

        ediot?.apply {
            //putString("list", Json.encodeToString(listSongs[songPlaying]))
            putString("music", nameSong)
            putString("by", by)
            putInt("duration", songDuration)
            putString("uriSong", uriSong.toString())
            apply()
        }

    }
}