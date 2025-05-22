/*
 * The MIT License (MIT)
 * Copyright (c) 2016. Sergio Leonardo Isasmendi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package leoisasmendi.android.com.suricatepodcast.data

import android.os.Parcel
import android.os.Parcelable


class PlaylistItem : Parcelable {
    val id: Int
    @JvmField
    val title: String?
    val showTitle: String?
    @JvmField
    val duration: String?
    @JvmField
    val poster: String?
    val audio: String?
    val description: String?
    private var isSelected: Boolean? = null
    var favorite: Boolean? = null
        private set

    private constructor(builder: Builder) {
        this.id = builder.id
        this.title = builder.title
        this.showTitle = builder.showTitle
        this.duration = builder.duration
        this.poster = builder.poster
        this.audio = builder.audio
        this.description = builder.description
        this.isSelected = builder.isSelected
        this.favorite = builder.isFavorite
    }

    fun getSelected(): Boolean {
        return isSelected!!
    }

    fun toggleSelected() {
        this.isSelected = !this.isSelected!!
    }


    /* BUILDER */
    class Builder(val id: Int) {
        var title: String? = null
        var showTitle: String? = null
        var duration: String? = null
        var poster: String? = null
        var audio: String? = null
        var description: String? = null
        var isSelected = false
        var isFavorite: Boolean? = false


        fun setTitle(title: String?): Builder {
            this.title = title
            return this
        }

        fun setShowTitle(showTitle: String?): Builder {
            this.showTitle = showTitle
            return this
        }

        fun setDuration(duration: String?): Builder {
            this.duration = duration
            return this
        }

        fun setPoster(poster: String?): Builder {
            this.poster = poster
            return this
        }

        fun setAudio(audio: String?): Builder {
            this.audio = audio
            return this
        }

        fun setDescription(description: String?): Builder {
            this.description = description
            return this
        }

        fun setSelected(selected: Boolean): Builder {
            isSelected = selected
            return this
        }

        fun setFavorite(favorite: Boolean?): Builder {
            isFavorite = favorite
            return this
        }

        fun build(): PlaylistItem {
            return PlaylistItem(this)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(this.id)
        dest.writeString(this.title)
        dest.writeString(this.showTitle)
        dest.writeString(this.duration)
        dest.writeString(this.poster)
        dest.writeString(this.audio)
        dest.writeString(this.description)
    }

    protected constructor(`in`: Parcel) {
        this.id = `in`.readInt()
        this.title = `in`.readString()
        this.showTitle = `in`.readString()
        this.duration = `in`.readString()
        this.poster = `in`.readString()
        this.audio = `in`.readString()
        this.description = `in`.readString()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PlaylistItem?> =
            object : Parcelable.Creator<PlaylistItem?> {
                override fun createFromParcel(source: Parcel): PlaylistItem {
                    return PlaylistItem(source)
                }

                override fun newArray(size: Int): Array<PlaylistItem?> {
                    return arrayOfNulls<PlaylistItem>(size)
                }
            }
    }
}