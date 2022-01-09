package com.example.musicapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.interfaces.OnClickItemListListenner
import com.example.musicapp.model.MusicData

class ListMusicAdapter(
    val context: Context, val listMusic: ArrayList<MusicData>,
    val listener: OnClickItemListListenner) : RecyclerView.Adapter<ListMusicAdapter.MusicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.view_songs, null)
        return MusicViewHolder(view, context, listener, parent);
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.bindData(listMusic.get(position), position)
    }

    override fun getItemCount(): Int {
        return listMusic.size
    }

    class MusicViewHolder(itemView: View,val context: Context, val listener: OnClickItemListListenner, val parent: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        //private var numSong: TextView
        private var titleSong : TextView
        private var byArtist: TextView

        init {
            titleSong = itemView.findViewById(R.id.titleSong)
            byArtist = itemView.findViewById(R.id.byArtist)

        }
        fun bindData(musicData: MusicData, pos: Int) {
            titleSong.setText(musicData.title)
            byArtist.setText(musicData.albumArtist)

            itemView.setOnClickListener{
                listener.onClickViewList(absoluteAdapterPosition, it, parent)
            }
        }
    }
}