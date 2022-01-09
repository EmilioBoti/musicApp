package com.example.musicapp.interactors

import android.os.Parcel
import android.os.Parcelable

class PlayInteractor(val playBackInput: PlayBackInput?): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(PlayBackInput::class.java.classLoader)
    ) {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(playBackInput)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlayInteractor> {
        override fun createFromParcel(parcel: Parcel): PlayInteractor {
            return PlayInteractor(parcel)
        }

        override fun newArray(size: Int): Array<PlayInteractor?> {
            return arrayOfNulls(size)
        }
    }

}