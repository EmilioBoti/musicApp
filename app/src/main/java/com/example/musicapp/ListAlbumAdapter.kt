package com.example.musicapp

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable

class ListAlbumAdapter(val context: Context, val listAlbum: ArrayList<AlbumData>, val listener: OnItemClickLisstener) :
    RecyclerView.Adapter<ListAlbumAdapter.AlbumViewHolder>() {

    interface OnItemClickLisstener{
        fun onSongsClick(pos: Int)
    }

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

    class AlbumViewHolder(itemView: View, val conte: Context,val listener: OnItemClickLisstener) : RecyclerView.ViewHolder(itemView) {
        lateinit var name: TextView

        init {
            name = itemView.findViewById(R.id.albumName)
        }

        fun bindDataAlbum(album: AlbumData) {
            name.setText(album.name)
            itemView.setOnClickListener{ it ->
                listener.onSongsClick(absoluteAdapterPosition)
            }
        }
    }
}