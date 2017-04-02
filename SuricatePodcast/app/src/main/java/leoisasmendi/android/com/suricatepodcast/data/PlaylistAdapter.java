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

import android.content.BroadcastReceiver;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import leoisasmendi.android.com.suricatepodcast.R;
import leoisasmendi.android.com.suricatepodcast.services.MediaPlayerService;
import leoisasmendi.android.com.suricatepodcast.ui.MainFragment;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

    private Cursor items;
    private MainFragment.OnFragmentInteractionListener mListener;

    private static final String ACTION = MediaPlayerService.ACTION_STOP;
    private BroadcastReceiver yourReceiver;

    public PlaylistAdapter(MainFragment.OnFragmentInteractionListener aListener) {
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
        holder.getCounterView().setText(String.format(Locale.US, "%d.", position));
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
        return items != null ? items.getCount() : 0;
    }

    // View Holder class
    public class PlaylistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

        public final View view;

        private TextView counterView;
        private TextView nameView;
        private TextView durationView;

        PlaylistViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            counterView = (TextView) itemView.findViewById(R.id.list_item_counter);
            nameView = (TextView) itemView.findViewById(R.id.playlist_item_name);
            durationView = (TextView) itemView.findViewById(R.id.playlist_item_length);
            itemView.setOnClickListener(this);
            view.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onClick(View view) {
            items.moveToPosition(getAdapterPosition());
            mListener.onClick(getAdapterPosition(), items);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select The Action");
            contextMenu.add(0, 1, 0, R.string.item_menu_delete).setOnMenuItemClickListener(mOnMyActionClickListener);
            contextMenu.add(0, 2, 0, R.string.item_menu_details).setOnMenuItemClickListener(mOnMyActionClickListener);
        }


        private final MenuItem.OnMenuItemClickListener mOnMyActionClickListener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                items.moveToPosition(getAdapterPosition());
                switch (item.getItemId()) {
                    case 1:
                        mListener.onDeleteItem(items.getInt(ItemLoader.Query.ID_PODCAST));
                        return true;
                    case 2:
                        mListener.onShowDetail(items);
                        return true;
                    default:
                        return true;
                }
            }
        };

        private TextView getNameView() {
            return nameView;
        }

        private TextView getDurationView() {
            return durationView;
        }

        private TextView getCounterView() {
            return counterView;
        }
    }
}