package com.example.musicapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.interfaces.OnClickItemListListenner
import com.example.musicapp.model.AlbumData

class ListAlbumAdapter(val context: Context, val listAlbum: ArrayList<AlbumData>, val listener: OnClickItemListListenner) :
    RecyclerView.Adapter<ListAlbumAdapter.AlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.view_album, null)
        return  AlbumViewHolder(view, context, listener, parent)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bindDataAlbum(listAlbum.get(position))
    }

    override fun getItemCount(): Int {
        return listAlbum.size
    }

    class AlbumViewHolder(
        itemView: View,
        val conte: Context,
        val listener: OnClickItemListListenner,
        val parent: ViewGroup
    ) : RecyclerView.ViewHolder(itemView) {
        lateinit var name: TextView
        lateinit var byArtist: TextView
        lateinit var imageAlbum: ImageView

        init {
            name = itemView.findViewById(R.id.albumName)
            byArtist = itemView.findViewById(R.id.byArtist)
            imageAlbum = itemView.findViewById(R.id.albumImg)
        }

        fun bindDataAlbum(album: AlbumData) {
            name.setText(album.name)
            byArtist.setText(album.by)

            //Log.d("uri", "${album.imageUri}")

            album.imageUri?.apply {
                imageAlbum.setImageURI(this)
            } ?: imageAlbum.setImageDrawable(conte.getDrawable(R.drawable.music_notes))

            //imageAlbum.setImageDrawable(conte.getDrawable(R.drawable.music_notes))

            itemView.setOnClickListener{ it ->
                listener.onClickViewList(absoluteAdapterPosition, it, parent)
            }
        }
    }
}