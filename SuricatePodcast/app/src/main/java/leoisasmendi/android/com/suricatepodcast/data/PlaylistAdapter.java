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
import leoisasmendi.android.com.suricatepodcast.ui.MainFragment;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

    private Cursor mCursor;
    private MainFragment.OnMainListInteractionListener mListener;
    private Context mContext;

    public PlaylistAdapter(Context context, Cursor aCursor, MainFragment.OnMainListInteractionListener listener) {
        mCursor = aCursor;
        mListener = listener;
        mContext = context;
    }

    @Override
    public void onBindViewHolder(final PlaylistViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        final int index = position;

        Picasso.with(mContext).setLoggingEnabled(true);
        Picasso.with(mContext)
                .load(mCursor.getString(ItemLoader.Query.POSTER))
                .placeholder(R.drawable.picture)
                .error(R.drawable.picture)
                .into(holder.posterView);


        holder.getNameView().setText(mCursor.getString(ItemLoader.Query.TITLE));
        holder.getDurationView().setText(mCursor.getString(ItemLoader.Query.DURATION));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (null != mListener) {
                    mListener.onClickFragmentInteraction(index);
                }

            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (null != mListener) {
                    mListener.onLongClickFragmentInteraction(mCursor);
                    return true;
                }

                return false;
            }
        });

    }

    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_playlist_item, parent, false);
        final PlaylistViewHolder mViewHolder = new PlaylistViewHolder(view);
        return mViewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    // View Holder
    public class PlaylistViewHolder extends RecyclerView.ViewHolder {

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
        }

        public TextView getNameView() {
            return nameView;
        }

        public TextView getDurationView() {
            return durationView;
        }

    }
}
