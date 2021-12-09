package com.example.musicapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.interfaces.OnClickItemListListenner

class ListAlbumAdapter(val context: Context, val listAlbum: ArrayList<AlbumData>, val listener: OnClickItemListListenner) :
    RecyclerView.Adapter<ListAlbumAdapter.AlbumViewHolder>() {

    /*interface OnItemClickLisstener{
        fun onSongsClick(pos: Int)
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAlbumAdapter.AlbumViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.view_album, null)
        return  AlbumViewHolder(view, context, listener)
    }

    override fun onBindViewHolder(holder: ListAlbumAdapter.AlbumViewHolder, position: Int) {
        holder.bindDataAlbum(listAlbum.get(position))
    }

    override fun getItemCount(): Int {
        return listAlbum.size
    }

    class AlbumViewHolder(itemView: View, val conte: Context,val listener: OnClickItemListListenner) : RecyclerView.ViewHolder(itemView) {
        lateinit var name: TextView
        lateinit var byArtist: TextView

        init {
            name = itemView.findViewById(R.id.albumName)
            byArtist = itemView.findViewById(R.id.byArtist)
        }

        fun bindDataAlbum(album: AlbumData) {
            name.setText(album.name)
            byArtist.setText(album.by)
            itemView.setOnClickListener{ it ->
                listener.onClickViewList(absoluteAdapterPosition)
            }
        }
    }
}