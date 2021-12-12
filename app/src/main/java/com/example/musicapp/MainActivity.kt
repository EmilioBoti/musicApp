package com.example.musicapp

import android.Manifest.permission.*
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.musicapp.interfaces.OnClickPlayerBar

data class MusicData(
    val id: Long?,
    val title: String?,
    val name: String?,
    val Album: String?,
    val AlbumArtist: String?,
    val mimeType: String?,
    val contentUri: Uri?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Uri::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(title)
        parcel.writeString(name)
        parcel.writeString(Album)
        parcel.writeString(AlbumArtist)
        parcel.writeString(mimeType)
        parcel.writeParcelable(contentUri, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MusicData> {
        override fun createFromParcel(parcel: Parcel): MusicData {
            return MusicData(parcel)
        }

        override fun newArray(size: Int): Array<MusicData?> {
            return arrayOfNulls(size)
        }
    }
}

data class AlbumData(
    val id: Long?,
    val name: String?,
    val by: String?,
    val contentUri: Uri?
)

class MainActivity : AppCompatActivity(), OnClickPlayerBar, MediaPlayer.OnCompletionListener {
    private var listMusic: ArrayList<MusicData> = ArrayList()
    lateinit var btnBack: ImageButton
    lateinit var btnPlay: ImageButton
    lateinit var btnNext: ImageButton
    lateinit var currentSong: TextView
    lateinit var byArtist: TextView
    lateinit var media: MediaPlayer
    var songPlaying: Int = 0

    val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when{
                ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED->{
                    callAlbumFragment()
                }else->{
                    Toast.makeText(applicationContext, "Permission denied 2", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        //request permision
        requestPermissionLauncher.launch(READ_EXTERNAL_STORAGE)
    }
    fun init(){
        btnBack = findViewById(R.id.btnBack)
        btnPlay = findViewById(R.id.btnPlay)
        btnNext = findViewById(R.id.btnNext)
        currentSong = findViewById(R.id.songPlaying)
        byArtist = findViewById(R.id.byArtist)
        media = MediaPlayer()
    }

    fun callAlbumFragment(){
        val albumListFragment = AlbumListFragment(this)
        val fragmentTransation = supportFragmentManager.beginTransaction()
        fragmentTransation.add(R.id.fragmentContainerView, albumListFragment)
            .commit()
    }

    override fun onClickBackBtn() {
        if(songPlaying > 0) songPlaying--
        playSong(songPlaying)
        modifyBarPlayer(songPlaying, getDrawable(R.drawable.pause_24))

    }

    override fun onClickPlayBtn(songPlaying: Int, media: MediaPlayer) {
       pauseSong()
    }
    override fun onClickNextBtn() {
        playNext()
    }

    override fun onClickSong(pos: Int, listSongs: ArrayList<MusicData>) {
        listMusic = listSongs
        songPlaying = pos
        modifyBarPlayer(songPlaying, getDrawable(R.drawable.pause_24))
        if(songPlaying <= listMusic.size) playSong(songPlaying)
    }
    private fun pauseSong(){
        if (this.media.isPlaying){
            this.media.pause()
            modifyBarPlayer(null, getDrawable(R.drawable.play_arrow_24))
        }else{
            modifyBarPlayer(null,getDrawable(R.drawable.pause_24))
            this.media.start()
        }
    }
    private fun playSong(songToPlay: Int){
        if (this.media.isPlaying){
            this.media.stop()
        }
        media = MediaPlayer.create(applicationContext, listMusic.get(songToPlay).contentUri)
        media.setOnCompletionListener(this)
        media.start()
    }

    private fun modifyBarPlayer(pos: Int?, drawable: Drawable?){
        btnPlay.setImageDrawable(drawable)
        if (pos != null){
            currentSong.setText(listMusic.get(pos).title)
            byArtist.setText(listMusic.get(pos).AlbumArtist)
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        playNext()
    }
    private fun playNext(){
        songPlaying++
        if (songPlaying >= listMusic.size ){
            songPlaying--
            playSong(songPlaying)
        }else{
            playSong(songPlaying)
        }
        modifyBarPlayer(songPlaying, getDrawable(R.drawable.pause_24))
    }
}