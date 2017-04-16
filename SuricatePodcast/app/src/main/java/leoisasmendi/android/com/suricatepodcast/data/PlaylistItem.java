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

package leoisasmendi.android.com.suricatepodcast.data;


import android.os.Parcel;
import android.os.Parcelable;

public class PlaylistItem implements Parcelable {

    private final int id;
    private final String title;
    private final String duration;
    private final String poster;
    private final String audio;
    private final String description;
    private Boolean isSelected;
    private Boolean isFavorite;

    private PlaylistItem(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.duration = builder.duration;
        this.poster = builder.poster;
        this.audio = builder.audio;
        this.description = builder.description;
        this.isSelected = builder.isSelected;
        this.isFavorite = builder.isFavorite;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }

    public String getPoster() {
        return poster;
    }

    public String getAudio() {
        return audio;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void toggleSelected() {
        this.isSelected = !this.isSelected;
    }


    /* BUILDER */
    public static class Builder {
        private final int id;
        private String title;
        private String duration;
        private String poster;
        private String audio;
        private String description;
        private Boolean isSelected;
        private Boolean isFavorite;


        public Builder(int id) {
            this.id = id;
        }

        public Builder setTitle(String title) {
            this.title = title;
            this.isSelected = false;
            this.isFavorite = false;
            return this;
        }

        public Builder setDuration(String duration) {
            this.duration = duration;
            return this;
        }

        public Builder setPoster(String poster) {
            this.poster = poster;
            return this;
        }

        public Builder setAudio(String audio) {
            this.audio = audio;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setSelected(Boolean selected) {
            isSelected = selected;
            return this;
        }

        public Builder setFavorite(Boolean favorite) {
            isFavorite = favorite;
            return this;
        }

        public PlaylistItem build() {
            return new PlaylistItem(this);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.duration);
        dest.writeString(this.poster);
        dest.writeString(this.audio);
        dest.writeString(this.description);
    }

    protected PlaylistItem(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.duration = in.readString();
        this.poster = in.readString();
        this.audio = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<PlaylistItem> CREATOR = new Parcelable.Creator<PlaylistItem>() {
        @Override
        public PlaylistItem createFromParcel(Parcel source) {
            return new PlaylistItem(source);
        }

        @Override
        public PlaylistItem[] newArray(int size) {
            return new PlaylistItem[size];
        }
    };
}