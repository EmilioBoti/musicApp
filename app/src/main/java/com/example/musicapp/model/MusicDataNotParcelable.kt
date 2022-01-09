package com.example.musicapp.model

import android.net.Uri

class MusicDataNotParcelable(val id: Long?,
                             val title: String?,
                             val name: String?,
                             val album: String?,
                             val albumArtist: String?,
                             val mimeType: String?,
                             val contentUri: Uri?)
