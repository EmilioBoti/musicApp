package com.example.musicapp.view

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.adapters.ListMusicAdapter
import com.example.musicapp.R
import com.example.musicapp.interfaces.OnClickItemListListenner
import com.example.musicapp.interfaces.OnClickPlayerBar
import com.example.musicapp.model.MusicData

class ListMusicFragment(val listenner: OnClickPlayerBar) : Fragment(), OnClickItemListListenner, View.OnClickListener {
    var listsongs: ArrayList<MusicData> = arrayListOf()
    lateinit var recyclerViewSong: RecyclerView
    lateinit var media: MediaPlayer
    lateinit var barPlay: RelativeLayout
    lateinit var btnBack: ImageButton
    lateinit var btnPlay: ImageButton
    lateinit var btnNext: ImageButton
    lateinit var btnGoBack: ImageButton
    lateinit var currentSong: TextView
    lateinit var currentAlbum: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_music, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        val data = arguments?.getString("currentAlbum")

        //get the list Music from an album
        listsongs = arguments?.getParcelableArrayList("list")!!
        currentAlbum.setText(data)

        val listmusicAdapter = ListMusicAdapter(activity?.applicationContext!!, listsongs, this)
        recyclerViewSong.apply {
            layoutManager = LinearLayoutManager(activity?.applicationContext, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(activity?.applicationContext, LinearLayoutManager.VERTICAL))
            adapter = listmusicAdapter
        }

        //event click
        btnGoBack.setOnClickListener(this)
        btnPlay.setOnClickListener(this)
        btnNext.setOnClickListener(this)
        btnBack.setOnClickListener(this)

    }
    private fun init(){
        recyclerViewSong = view!!.findViewById(R.id.songsContaienr)
        btnGoBack = view!!.findViewById(R.id.goBack)
        barPlay = activity?.findViewById(R.id.playBar)!!
        barPlay = activity?.findViewById(R.id.playBar)!!
        btnBack = activity?.findViewById(R.id.btnBack)!!
        btnPlay = activity?.findViewById(R.id.btnPlay)!!
        btnNext = activity?.findViewById(R.id.btnNext)!!
        currentSong = activity?.findViewById(R.id.songPlaying)!!
        currentAlbum = view!!.findViewById(R.id.currentAlbum)
        val s: Toolbar  = activity?.findViewById(R.id.toolbarMain)!!
        s.visibility = View.GONE
        //media = MediaPlayer()
    }

    override fun onClickViewList(pos: Int, view: View, parent: ViewGroup) {
        listenner.onClickSong(pos, listsongs )
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnBack ->{
                listenner.onClickBackBtn()
            }
            R.id.btnPlay ->{
                listenner.onClickPlayBtn()
            }
            R.id.btnNext ->{
                listenner.onClickNextBtn()
            }
            R.id.goBack ->{
                parentFragmentManager.popBackStack()
            }
        }
    }

}