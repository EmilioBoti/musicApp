package com.example.musicapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import com.example.musicapp.interactors.PlayBackInput
import com.example.musicapp.interactors.PlayInteractor
import com.example.musicapp.model.MusicData

class PlayerActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var btnBack: ImageButton
    lateinit var btnPlay: ImageButton
    lateinit var btnNext: ImageButton
    var playBackInput: PlayBackInput? = null
    var playInteractor: PlayInteractor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)



        //val musicData: MusicData? = intent.getParcelableExtra<MusicData>("inter")
        playInteractor = intent.getParcelableExtra<PlayInteractor>("inter")
        //playBackInput = player?.player

        init()
        //playBackInput = intent.getParcelableExtra<PlayBackInput>("inter") as PlayBackInput
        //Toast.makeText(this, "${musicData?.title}", Toast.LENGTH_SHORT).show()

    }

    override fun onStart() {
        super.onStart()
        btnBack.setOnClickListener(this)
        btnPlay.setOnClickListener(this)
        btnNext.setOnClickListener(this)
    }

    private fun init(){
        btnBack = findViewById(R.id.btnBack)
        btnPlay = findViewById(R.id.btnPlay)
        btnNext = findViewById(R.id.btnNext)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnBack ->{
                //playBackInput?.onClickBackBtn()
                Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show()
            }
            R.id.btnPlay ->{
                Toast.makeText(this, "Play ", Toast.LENGTH_SHORT).show()
            }
            R.id.btnNext ->{
                //playBackInput?.onClickNextBtn()
                Toast.makeText(this, "Next", Toast.LENGTH_SHORT).show()
            }
        }
    }
}