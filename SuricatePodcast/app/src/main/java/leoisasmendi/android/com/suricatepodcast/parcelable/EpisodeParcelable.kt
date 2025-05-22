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
package leoisasmendi.android.com.suricatepodcast.parcelable

import android.os.Parcel
import android.os.Parcelable


class EpisodeParcelable : Parcelable {
    private var id = 0
    var title: String? = null
    var showTitle: String? = null
    var detail: String? = null
    var duration: String? = null
    var poster: String? = null
    var description: String? = null

    // No-arg Ctor
    constructor()

    /**
     * Ctor from Parcel, reads back fields IN THE ORDER they were written
     */
    constructor(pc: Parcel) {
        id = pc.readInt()
        title = pc.readString()
        showTitle = pc.readString()
        detail = pc.readString()
        duration = pc.readString()
        poster = pc.readString()
        description = pc.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(pc: Parcel, flags: Int) {
        pc.writeLong(id.toLong())
        pc.writeString(title)
        pc.writeString(showTitle)
        pc.writeString(detail)
        pc.writeString(duration)
        pc.writeString(poster)
        pc.writeString(description)
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getId(): Long {
        return id.toLong()
    }

    companion object {
        /**
         * Static field used to regenerate object, individually or as arrays
         */
        val CREATOR: Parcelable.Creator<EpisodeParcelable?> =
            object : Parcelable.Creator<EpisodeParcelable?> {
                override fun createFromParcel(pc: Parcel): EpisodeParcelable {
                    return EpisodeParcelable(pc)
                }

                override fun newArray(size: Int): Array<EpisodeParcelable?> {
                    return arrayOfNulls<EpisodeParcelable>(size)
                }
            }
    }
}