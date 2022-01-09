package com.example.musicapp.view

import android.Manifest.permission.*
import android.animation.ValueAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.*
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.example.musicapp.PlayerActivity
import com.example.musicapp.interactors.PlayBackInput
import com.example.musicapp.R
import com.example.musicapp.interactors.PlayInteractor
import com.example.musicapp.interfaces.UpdateUI
import com.example.musicapp.model.MusicData

class MainActivity : AppCompatActivity(), View.OnClickListener, UpdateUI {
    private lateinit var btnBack: ImageButton
    private lateinit var btnPlay: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var currentSong: TextView
    private lateinit var animMusic: LottieAnimationView
    private lateinit var byArtist: TextView
    private lateinit var containerCurrentSong: RelativeLayout
    private lateinit var playBackInput: PlayBackInput
    private lateinit var progressBar: SeekBar

    val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) -> {
                    callAlbumFragment()
                }
                else -> {
                    Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //get UI Objects
        init()

        //request permision to access to media files
        requestPermissionLauncher.launch(READ_EXTERNAL_STORAGE)

        btnBack.setOnClickListener(this)
        btnPlay.setOnClickListener(this)
        btnNext.setOnClickListener(this)
        containerCurrentSong.setOnClickListener(this)


    }
    private fun init(){
        animMusic = findViewById(R.id.animMusic)
        btnBack = findViewById(R.id.btnBack)
        btnPlay = findViewById(R.id.btnPlay)
        btnNext = findViewById(R.id.btnNext)
        progressBar = findViewById(R.id.progressBar)
        currentSong = findViewById(R.id.songPlaying)
        byArtist = findViewById(R.id.byArtist)
        containerCurrentSong = findViewById(R.id.currentSong)
        playBackInput = PlayBackInput(applicationContext, this)

    }


    private fun callAlbumFragment(){
        val albumListFragment = AlbumListFragment(playBackInput)
        val fragmentTransation = supportFragmentManager.beginTransaction()
        fragmentTransation.add(R.id.fragmentContainerView, albumListFragment)
            .commit()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnBack ->{
                playBackInput.onClickBackBtn()
            }
            R.id.btnPlay ->{
                playBackInput.onClickPlayBtn()
            }
            R.id.btnNext ->{
                playBackInput.onClickNextBtn()
            }
            R.id.currentSong ->{
                //val m = MusicData(null,"testing",null, null,null,null,null)
                //containerCurrentSong.visibility = View.GONE
                //val intent = Intent(this, PlayerActivity::class.java)
                /*intent.apply {
                    //putExtra("inte", bundle)
                   // putExtra("inter", playInteractor)
                }*/
                //startActivity(intent)
            }
        }
    }
    /*private fun clearData(){
        val share = applicationContext?.getSharedPreferences(applicationContext?.getString(R.string.prefs_file)
            , Context.MODE_PRIVATE)

        share?.edit {
            clear()
        }
    }*/

    override fun modifyBarPlayer(pos: Int?, drawable: Drawable?, musicData: MusicData) {
        btnPlay.setImageDrawable(drawable)

        pos?.apply {
            currentSong.text = musicData.title
            byArtist.text = musicData.albumArtist
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

    override fun updateProgressBar(current: Int, duration: Int) {
        progressBar.max = duration
        //Log.d("dur", "${duration/60}")
        progressBar.progress = current
    }

}
