package com.example.musicapp.interfaces

import android.view.View
import android.view.ViewGroup

interface OnClickItemListListenner {
    fun onClickViewList(pos: Int, view: View, parent: ViewGroup)
}