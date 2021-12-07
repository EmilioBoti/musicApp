package com.example.musicapp

import android.Manifest.permission.*
import android.content.ContentUris
import android.content.Intent
import android.content.LocusId
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileNotFoundException
import java.io.Serializable
import java.security.Permission
import java.util.jar.Manifest

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
    val contentUri: Uri?
)

class MainActivity : AppCompatActivity(), ListAlbumAdapter.OnItemClickLisstener {
    private val listMusic: ArrayList<MusicData> = ArrayList()
    private val listAlbumes: ArrayList<AlbumData> = ArrayList()
    lateinit var recyclerView: RecyclerView

    val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when{
                ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED->{
                    //getInternalStorageMusic()
                    getInternalStorageAlbum()
                    val musicadapter = ListAlbumAdapter(this, listAlbumes, this)
                    //recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    recyclerView.layoutManager = GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)
                    recyclerView.adapter = musicadapter
                }
                else->{
                    Toast.makeText(applicationContext, "Permission denied 2", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycleviewContainer)
        //request permision
        requestPermissionLauncher.launch(READ_EXTERNAL_STORAGE)

    }
    private fun getInternalStorageAlbum(){

        val columnsAlbums = arrayOf(MediaStore.Audio.Albums.ALBUM_ID,MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS
        )

        val cursorAlbum: Cursor? = applicationContext.contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            columnsAlbums, null, null, null)


        cursorAlbum?.use {

            val idAlbumColumns = it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ID)
            val nameAlbumColumns = it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)
            val numberSongsColumns = it.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS)
            //val contentTypeColumns = it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ENTRY_CONTENT_TYPE)

            while (it.moveToNext()){

                var idAlbum = it.getLong(idAlbumColumns)
                var nameAlbum = it.getString(nameAlbumColumns)
                var numSong = it.getInt(numberSongsColumns)
                //var contentType = it.getString(contentTypeColumns)
                val contentUris: Uri = ContentUris.withAppendedId(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, idAlbum)

                if(numSong >= 3 && !nameAlbum.equals("WhatsApp Audio") ) listAlbumes.add(AlbumData(idAlbum, nameAlbum, contentUris))
                //Log.d("album", "${nameAlbum}: songs: ${contentType}")
            }
        }


    }
    private fun getInternalStorageMusic(){

        val columns = arrayOf(MediaStore.Audio.Media._ID,MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ALBUM_ARTIST
            )

        val cursor: Cursor? = applicationContext.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            columns, null, null, null)

        cursor.use { cursor ->

            val idColumns = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumns = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val nameColumns = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val mimeTypeColumns = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)
            val albumColumns = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val albumArtistColumns = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ARTIST)

            Toast.makeText(applicationContext, "list count: ${cursor?.count}", Toast.LENGTH_SHORT).show()

            while(cursor?.moveToNext() == true){

                val id = idColumns?.let { cursor?.getLong(it) }
                val title = titleColumns?.let { cursor?.getString(it) }
                val album = albumColumns?.let { cursor?.getString(it) }
                val albumArtist = albumArtistColumns?.let { cursor?.getString(it) }

                val name = nameColumns?.let {
                    cursor?.getString(it)
                }
                val mimetype = mimeTypeColumns?.let { cursor?.getString(mimeTypeColumns) }
                val contentUri = id?.let { ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, it.toLong()) }

                if (name?.endsWith(".mp3")!!){
                    //album?.let { Log.d("musicA", it) }
                    listMusic.add(MusicData(id, title, name, album, albumArtist ,mimetype, contentUri))
                }
            }
        }
    }

    override fun onSongsClick(pos: Int) {

        val columnsAlbums = arrayOf(
            MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DISPLAY_NAME,MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ALBUM_ARTIST,
            MediaStore.Audio.Media.ALBUM_ID
        )
        //Toast.makeText(conte, "Working", Toast.LENGTH_SHORT).show()

        val cursorAlbum: Cursor? = applicationContext.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            , columnsAlbums, null, null, null)

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
            callListsongsActivity()
        }
    }

    fun callListsongsActivity(){
        val data = Bundle()
        data.putParcelableArrayList("list",listMusic)

        val intent: Intent = Intent(applicationContext, Listsongs::class.java).apply {
            putExtras(data)
        }
        startActivity(intent)
    }
}