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

package leoisasmendi.android.com.suricatepodcast.parcelable;


import android.os.Parcel;
import android.os.Parcelable;

public class EpisodeParcelable implements Parcelable {

    /**
     * Static field used to regenerate object, individually or as arrays
     */
    public static final Parcelable.Creator<EpisodeParcelable> CREATOR = new Parcelable.Creator<EpisodeParcelable>() {
        public EpisodeParcelable createFromParcel(Parcel pc) {
            return new EpisodeParcelable(pc);
        }

        public EpisodeParcelable[] newArray(int size) {
            return new EpisodeParcelable[size];
        }
    };

    private int id;
    private String title;
    private String showTitle;
    private String detail;
    private String duration;
    private String poster;
    private String description;

    // No-arg Ctor
    public EpisodeParcelable() {
    }

    /**
     * Ctor from Parcel, reads back fields IN THE ORDER they were written
     */
    public EpisodeParcelable(Parcel pc) {
        id = pc.readInt();
        title = pc.readString();
        showTitle = pc.readString();
        detail = pc.readString();
        duration = pc.readString();
        poster = pc.readString();
        description = pc.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel pc, int flags) {
        pc.writeLong(id);
        pc.writeString(title);
        pc.writeString(showTitle);
        pc.writeString(detail);
        pc.writeString(duration);
        pc.writeString(poster);
        pc.writeString(description);
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShowTitle() {
        return showTitle;
    }

    public void setShowTitle(String showTitle) {
        this.showTitle = showTitle;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}