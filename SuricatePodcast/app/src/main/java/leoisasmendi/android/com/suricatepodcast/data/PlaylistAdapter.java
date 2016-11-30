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

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import leoisasmendi.android.com.suricatepodcast.R;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

    private List<PodcastItem> playlist;

    public PlaylistAdapter(List<PodcastItem> aPlaylist) {
        playlist = aPlaylist;
    }

    @Override
    public void onBindViewHolder(PlaylistViewHolder holder, int position) {

        Log.i("PlaylistAdapter", "onBindViewHolder: " + position);
        holder.getNameView().setText(playlist.get(position).getName());
        holder.getLengthView().setText(playlist.get(position).getLength());
    }

    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_playlist_item, parent, false);
        PlaylistViewHolder playlistViewHolder = new PlaylistViewHolder(view);
        return playlistViewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return playlist.size();
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        private TextView nameView;
        private TextView lengthView;

        PlaylistViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.playlist_item_name);
            lengthView = (TextView) itemView.findViewById(R.id.playlist_item_length);
        }

        public TextView getNameView() {
            return nameView;
        }

        public TextView getLengthView() {
            return lengthView;
        }
    }
}
