package com.example.musicapp

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class ListMusicAdapter(val context: Context, val listMusic: ArrayList<MusicData>,val listener: OnMusicClickListener) : RecyclerView.Adapter<ListMusicAdapter.MusicViewHolder>() {

    interface OnMusicClickListener{
        fun onclickMusic(pos: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.view_songs, null)
        return MusicViewHolder(view, context, listener);
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.bindData(listMusic.get(position))
    }

    override fun getItemCount(): Int {
        return listMusic.size
    }

    class MusicViewHolder(itemView: View,val context: Context, val listener: OnMusicClickListener) : RecyclerView.ViewHolder(itemView) {
        lateinit var titleSong : TextView
        lateinit var byArtist: TextView
        lateinit var media: MediaPlayer

        init {
            titleSong = itemView.findViewById(R.id.titleSong)
            byArtist = itemView.findViewById(R.id.byArtist)
        }
        fun bindData(musicData: MusicData) {
            titleSong.setText(musicData.title)
            byArtist.setText(musicData.AlbumArtist)

            itemView.setOnClickListener{
                listener.onclickMusic(absoluteAdapterPosition)
            }
        }
    }
}