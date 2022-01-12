package com.example.musicapp.view

import android.Manifest.permission.*
import android.animation.ValueAnimator
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.*
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.musicapp.interactors.PlayBackInput
import com.example.musicapp.R
import com.example.musicapp.interfaces.UpdateUI
import com.example.musicapp.model.MusicData

class MainActivity : AppCompatActivity(), View.OnClickListener,SeekBar.OnSeekBarChangeListener, UpdateUI {
    private lateinit var btnBack: ImageButton
    private lateinit var btnPlay: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var currentSong: TextView
    private lateinit var animMusic: LottieAnimationView
    private lateinit var byArtist: TextView
    private lateinit var containerCurrentSong: RelativeLayout
    private lateinit var playBackInput: PlayBackInput
    private lateinit var progressBar: SeekBar
    val channelId = "com.example.musicapp"
    val notificationId = 1

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
        createNotificationChannel()
       // showNotifycationMusic()

        btnBack.setOnClickListener(this)
        btnPlay.setOnClickListener(this)
        btnNext.setOnClickListener(this)
        containerCurrentSong.setOnClickListener(this)

        progressBar.setOnSeekBarChangeListener(this)

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
    private fun createNotificationChannel(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "com.example.musicapp"
            val descriptionText = "music notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            //register channel in the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotifycationMusic(){

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.music_note_24)
            .setContentTitle("My notification")
            .setContentText("Hello World!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
        }
    }

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
        progressBar.progress = current
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        seekBar?.progress?.let {
            playBackInput.onTouchProgressbar(it)
            progressBar.progress = it
        }
    }

}
