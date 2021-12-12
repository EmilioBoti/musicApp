package com.example.musicapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.interfaces.OnClickItemListListenner

class ListMusicAdapter(
    val context: Context, val listMusic: ArrayList<MusicData>,
    val listener: OnClickItemListListenner) : RecyclerView.Adapter<ListMusicAdapter.MusicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.view_songs, null)
        return MusicViewHolder(view, context, listener);
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.bindData(listMusic.get(position), position)
    }

    override fun getItemCount(): Int {
        return listMusic.size
    }

    class MusicViewHolder(itemView: View,val context: Context, val listener: OnClickItemListListenner) : RecyclerView.ViewHolder(itemView) {
        //private var numSong: TextView
        private var titleSong : TextView
        private var byArtist: TextView
        init {
            titleSong = itemView.findViewById(R.id.titleSong)
            byArtist = itemView.findViewById(R.id.byArtist)
            //numSong = itemView.findViewById(R.id.numSong)
        }
        fun bindData(musicData: MusicData, pos: Int) {
            titleSong.setText(musicData.title)
            byArtist.setText(musicData.AlbumArtist)
            var position = pos
            position++
            //numSong.setText(position.toString())

            itemView.setOnClickListener{
                listener.onClickViewList(absoluteAdapterPosition)
            }
        }
    }
}