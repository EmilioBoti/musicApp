package com.example.musicapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.adapters.ListAlbumAdapter
import com.example.musicapp.R
import com.example.musicapp.interfaces.OnClickItemListListenner
import com.example.musicapp.interfaces.OnClickPlayerBar
import com.example.musicapp.model.AlbumData
import com.example.musicapp.model.ModelData
import com.example.musicapp.model.MusicData

class AlbumListFragment (val listenner: OnClickPlayerBar): Fragment(), OnClickItemListListenner {
    lateinit private var listMusic: ArrayList<MusicData>
    lateinit private var listAlbumes: ArrayList<AlbumData>
    lateinit var recyclerView: RecyclerView
    var modelData: ModelData? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
    }
    fun init(view: View){
        recyclerView = view.findViewById(R.id.recycleviewContainer)
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbarMain)
        modelData = ModelData()
        toolbar?.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()

        //get all albumes
        listAlbumes = activity?.let {
            modelData?.getExternalStorageAlbum(it)
        }!!

        if(listAlbumes.isEmpty()){
            val notFoundFragment = NotFoundFragment()
            val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, notFoundFragment)
            fragmentTransaction?.commit()

        }else{
            val musicadapter = ListAlbumAdapter(activity!!.applicationContext, listAlbumes, this)
            recyclerView.apply {
                layoutManager = GridLayoutManager(activity!!.applicationContext, 2, LinearLayoutManager.VERTICAL, false)
                adapter = musicadapter
            }
        }
    }

    override fun onClickViewList(pos: Int, view: View, parent: ViewGroup) {
        listMusic = arrayListOf()

        //get all music from the album selected
        listMusic = activity?.let { activity->
            listAlbumes[pos].id?.let { id ->
                modelData?.getMusicFromAlbum(id, activity)
            }
        }!!
        callMusicListFragment(pos)
    }

    private fun callMusicListFragment(pos: Int){

        val data = Bundle().apply {
            putParcelableArrayList("list", listMusic)
            putString("currentAlbum", listAlbumes.get(pos).name)
        }
        var listMusicFragment = ListMusicFragment(listenner).apply {
            arguments = data
        }

        var fragmentTransation = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransation?.replace(R.id.fragmentContainerView, listMusicFragment)
            ?.addToBackStack("back")
            ?.commit()
    }
}