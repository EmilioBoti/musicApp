package com.example.musicapp.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable


data class MusicData(
    val id: Long?,
    val title: String?,
    val name: String?,
    val album: String?,
    val albumArtist: String?,
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
        parcel.writeString(album)
        parcel.writeString(albumArtist)
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


