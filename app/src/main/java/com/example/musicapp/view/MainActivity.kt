package com.example.musicapp.view

import android.Manifest.permission.*
import android.animation.ValueAnimator
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.*
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import com.airbnb.lottie.LottieAnimationView
import com.example.musicapp.interactors.PlayBackImput
import com.example.musicapp.R
import com.example.musicapp.interfaces.UpdateUI
import com.example.musicapp.model.MusicData
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), View.OnClickListener, UpdateUI {
    private var listMusic: ArrayList<MusicData> = ArrayList()
    lateinit var btnBack: ImageButton
    lateinit var btnPlay: ImageButton
    lateinit var btnNext: ImageButton
    lateinit var currentSong: TextView
    lateinit var animMusic: LottieAnimationView
    lateinit var byArtist: TextView
    lateinit var containerCurrentSong: RelativeLayout
    //var listViewContainer: ViewGroup? = null
    lateinit var media: MediaPlayer
    var songPlaying: Int = 0
    var durationSong: Int = 0
    lateinit var plabackImput: PlayBackImput

    val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when{
                ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED->{
                    callAlbumFragment()
                }else->{
                    Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        //request permision to access to media files
        requestPermissionLauncher.launch(READ_EXTERNAL_STORAGE)

        btnBack.setOnClickListener(this)
        btnPlay.setOnClickListener(this)
        btnNext.setOnClickListener(this)

    }
    private fun init(){
        animMusic = findViewById(R.id.animMusic)
        btnBack = findViewById(R.id.btnBack)
        btnPlay = findViewById(R.id.btnPlay)
        btnNext = findViewById(R.id.btnNext)
        currentSong = findViewById(R.id.songPlaying)
        byArtist = findViewById(R.id.byArtist)
        containerCurrentSong = findViewById(R.id.currentSong)
        media = MediaPlayer()
        plabackImput = PlayBackImput(applicationContext, this)

    }


    private fun callAlbumFragment(){
        val albumListFragment = AlbumListFragment(plabackImput)
        val fragmentTransation = supportFragmentManager.beginTransaction()
        fragmentTransation.add(R.id.fragmentContainerView, albumListFragment)
            .commit()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnBack ->{
                plabackImput.onClickBackBtn()
            }
            R.id.btnPlay ->{
                plabackImput.onClickPlayBtn()
            }
            R.id.btnNext ->{
                plabackImput.onClickNextBtn()
            }
        }
    }
    private fun clearData(){
        val share = applicationContext?.getSharedPreferences(applicationContext?.getString(R.string.prefs_file)
            , Context.MODE_PRIVATE)

        share?.edit {
            clear()
        }
    }

    override fun modifyBarPlayer(pos: Int?, drawable: Drawable?, musicData: MusicData) {
        btnPlay.setImageDrawable(drawable)

        pos?.apply {
            currentSong.text = musicData.title
            byArtist.text = musicData.AlbumArtist
        }
    }

    override fun animMusicView(toPlay: String) {
        when(toPlay){
            "pause"->{
                animMusic.repeatCount = 0
            }
            "play"->{
                animMusic.repeatCount = ValueAnimator.INFINITE
                animMusic.playAnimation()
            }
        }
    }

    override fun currentSongPlaying(title: String, by: String) {
        currentSong.text = title
        byArtist.text = by
    }

}
