package com.example.musicapp

import android.content.ContentUris
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.interfaces.OnClickItemListListenner
import com.example.musicapp.interfaces.OnClickPlayerBar

class AlbumListFragment (val listenner: OnClickPlayerBar): Fragment(), OnClickItemListListenner {
    lateinit private var listMusic: ArrayList<MusicData>
    lateinit private var listAlbumes: ArrayList<AlbumData>
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)

        getInternalStorageAlbum()
        val musicadapter = ListAlbumAdapter(activity!!.applicationContext, listAlbumes, this)
        recyclerView.apply {
            layoutManager = GridLayoutManager(activity!!.applicationContext, 2, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(activity?.applicationContext, LinearLayoutManager.VERTICAL))
            adapter = musicadapter
        }
    }
    fun init(view: View){
        recyclerView = view.findViewById(R.id.recycleviewContainer)
    }

    private fun getInternalStorageAlbum(){
        listAlbumes = arrayListOf()
        val columnsAlbums = arrayOf(
            MediaStore.Audio.Albums.ALBUM_ID, MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS
        )

        val cursorAlbum: Cursor? = activity?.applicationContext?.contentResolver?.query(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            columnsAlbums, null, null, null)

        cursorAlbum?.use {

            val idAlbumColumns = it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ID)
            val nameAlbumColumns = it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)
            val numberSongsColumns = it.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS)
            val byAtist = it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST)

            while (it.moveToNext()){

                var idAlbum = it.getLong(idAlbumColumns)
                var nameAlbum = it.getString(nameAlbumColumns)
                var numSong = it.getInt(numberSongsColumns)
                var by = it.getString(byAtist)
                val contentUris: Uri = ContentUris.withAppendedId(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, idAlbum)

                if(numSong >= 3 && !nameAlbum.equals("WhatsApp Audio") ) listAlbumes.add(AlbumData(idAlbum, nameAlbum,by ,contentUris))
            }
        }
    }

    override fun onClickViewList(pos: Int) {
        listMusic = arrayListOf()

        val columnsAlbums = arrayOf(
            MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ALBUM_ARTIST,
            MediaStore.Audio.Media.ALBUM_ID
        )

        val cursorAlbum: Cursor? = activity?.applicationContext?.contentResolver?.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            ,columnsAlbums, null, null, null)

        cursorAlbum.use {

            val idAlbumColumns = it?.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val idColumns = it?.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumns = it?.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val nameColumns = it?.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val mimeTypeColumns = it?.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)
            val albumColumns = it?.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val albumArtistColumns = it?.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ARTIST)

            while (it?.moveToNext() == true){

                val idAlbum = idAlbumColumns?.let { it1 -> it?.getLong(it1) }
                val id = idColumns?.let { it1 -> it?.getLong(it1) }
                val title = titleColumns?.let { it1 ->it?.getString(it1) }
                val album2 = albumColumns?.let { it1 -> it?.getString(it1) }
                val albumArtist = albumArtistColumns?.let { it1 -> it?.getString(it1) }
                val name = nameColumns?.let { it1 -> it?.getString(it1) }
                val mimetype = mimeTypeColumns?.let { it1 -> it?.getString(mimeTypeColumns) }
                val contentUri = id?.let { ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, it) }

                if (listAlbumes.get(pos).id == idAlbum ) {
                    listMusic.add(MusicData(id, title, name, album2, albumArtist ,mimetype, contentUri))
                }
            }
            callMusicListFragment(pos)
        }
    }

    fun callMusicListFragment(pos: Int){
        Toast.makeText(activity?.applicationContext, "ok", Toast.LENGTH_SHORT).show()
        val data = Bundle().apply {
            putParcelableArrayList("list", listMusic)
            putString("currentAlbum", listAlbumes.get(pos).name)
        }
        var listMusicFragment = ListMusicFragment(listenner).apply {
            arguments = data
        }

        var fragmentTransation = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransation
            ?.replace(R.id.fragmentContainerView, listMusicFragment)
            ?.addToBackStack("back")
            ?.commit()
    }
}