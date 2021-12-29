package com.example.musicapp.model

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.fragment.app.FragmentActivity

class ModelData() {
    lateinit private var listMusic: ArrayList<MusicData>
    lateinit private var listAlbumes: ArrayList<AlbumData>

    fun getExternalStorageAlbum(activity: FragmentActivity): ArrayList<AlbumData>{

        listAlbumes = arrayListOf()

        val columnsAlbums = arrayOf(
            MediaStore.Audio.Albums.ALBUM_ID, MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS
        )

        val cursorAlbum: Cursor? = activity.contentResolver?.query(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            columnsAlbums, null, null, null)

        cursorAlbum?.use {

            val idAlbumColumns = it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ID)
            val nameAlbumColumns = it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)
            val byAtist = it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST)
            val numberSongsColumns = it.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS)

            val uri: Uri = Uri.parse("content://media/external/audio/albumart");

            while (it.moveToNext()){

                val idAlbum = it.getLong(idAlbumColumns)
                val nameAlbum = it.getString(nameAlbumColumns)
                val numSong = it.getInt(numberSongsColumns)
                var by = it.getString(byAtist)

                val contentUris: Uri = ContentUris.withAppendedId(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, idAlbum)
                val contentUriImage: Uri = ContentUris.withAppendedId(uri, idAlbum)

                if(numSong >= 3 && !nameAlbum.equals("WhatsApp Audio") ) {
                    listAlbumes.add(AlbumData(idAlbum, nameAlbum,by ,contentUris, contentUriImage))
                }
            }
        }
        return listAlbumes
    }


    fun getMusicFromAlbum(albumId: Long, activity: FragmentActivity): ArrayList<MusicData>?{
        listMusic = arrayListOf()

        val columnsAlbums = arrayOf(
            MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ALBUM_ARTIST,
            MediaStore.Audio.Media.ALBUM_ID
        )

        val cursorAlbum: Cursor? = activity.applicationContext.contentResolver.query(
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

                val idAlbum = idAlbumColumns?.let { it1 -> it.getLong(it1) }
                val id = idColumns?.let { it1 -> it.getLong(it1) }
                val title = titleColumns?.let { it1 -> it.getString(it1) }
                val album2 = albumColumns?.let { it1 -> it.getString(it1) }
                var albumArtist = albumArtistColumns?.let { it1 -> it.getString(it1) } ?: "Unknow"
                var name = nameColumns?.let { it1 -> it.getString(it1) }
                val mimetype = mimeTypeColumns?.let { it1 -> it.getString(it1) }

                val contentUri = id?.let { ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, it) }

                if (albumId == idAlbum ) {
                    listMusic.add(MusicData(id, title, name, album2, albumArtist ,mimetype, contentUri))
                }
            }
        }
        return listMusic;
    }

}