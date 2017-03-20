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

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import leoisasmendi.android.com.suricatepodcast.R;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

    private Cursor items;
    private final Context mContext;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onClick(int position);

        void onLongClick(Cursor item);
    }

    public PlaylistAdapter(Context context, OnItemClickListener aListener) {
        this.mContext = context;
        this.mListener = aListener;
    }

    public void swapCursor(Cursor aCursor) {
        if (aCursor != null) {
            items = aCursor;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(final PlaylistViewHolder holder, int position) {
        items.moveToPosition(position);
        Picasso.with(mContext).setLoggingEnabled(true);
        Picasso.with(mContext)
                .load(items.getString(ItemLoader.Query.POSTER))
                .placeholder(R.drawable.picture)
                .error(R.drawable.picture)
                .into(holder.posterView);
        holder.getNameView().setText(items.getString(ItemLoader.Query.TITLE));
        holder.getDurationView().setText(items.getString(ItemLoader.Query.DURATION));
    }

    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_playlist_item, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return items.getCount();
    }

    // View Holder class
    public class PlaylistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public final View view;

        private TextView nameView;
        private TextView durationView;
        private ImageView posterView;

        PlaylistViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            nameView = (TextView) itemView.findViewById(R.id.playlist_item_name);
            durationView = (TextView) itemView.findViewById(R.id.playlist_item_length);
            posterView = (ImageView) itemView.findViewById(R.id.playlist_item_poster);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View view) {
            mListener.onClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            if (null != mListener) {
                mListener.onLongClick(items);
                return true;
            }

            return false;
        }

        public TextView getNameView() {
            return nameView;
        }

        public TextView getDurationView() {
            return durationView;
        }

    }
}