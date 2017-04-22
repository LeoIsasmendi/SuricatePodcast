/*
 *
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
 *
 */

package leoisasmendi.android.com.suricatepodcast.data;

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
import leoisasmendi.android.com.suricatepodcast.ui.MainFragment;

public class PlaylistCursorAdapter extends RecyclerViewCursorAdapter<PlaylistCursorAdapter.ViewHolder> {

    private MainFragment.OnFragmentInteractionListener mListener;


    public PlaylistCursorAdapter(MainFragment.OnFragmentInteractionListener aListener) {
        this.mListener = aListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        holder.bindData(cursor);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_playlist_item, parent, false);
        return new PlaylistCursorAdapter.ViewHolder(view);
    }


    // ViewHolder class
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

        private Cursor mCursor;
        private TextView counterView;
        private TextView showTitleView;
        private TextView nameView;
        private TextView durationView;

        private final MenuItem.OnMenuItemClickListener mOnMyActionClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            counterView = (TextView) itemView.findViewById(R.id.list_item_counter);
            showTitleView = (TextView) itemView.findViewById(R.id.playlist_item_show_name);
            nameView = (TextView) itemView.findViewById(R.id.playlist_item_name);
            durationView = (TextView) itemView.findViewById(R.id.playlist_item_length);
            mOnMyActionClickListener = getMenuItemListener();
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }


        public void bindData(final Cursor cursor) {
            mCursor = cursor;
            counterView.setText(String.format(Locale.US, "%d.", getAdapterPosition()));
            nameView.setText(mCursor.getString(ItemLoader.Query.TITLE));
            showTitleView.setText(mCursor.getString(ItemLoader.Query.SHOW_TITLE));
            durationView.setText(mCursor.getString(ItemLoader.Query.DURATION));
        }

        @Override
        public void onClick(View view) {
            mCursor.moveToPosition(getAdapterPosition());
            mListener.onClick(getAdapterPosition(), mCursor);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle(R.string.context_menu_title);
            contextMenu.add(0, 1, 0, R.string.item_menu_delete).setOnMenuItemClickListener(mOnMyActionClickListener);
            contextMenu.add(0, 2, 0, R.string.item_menu_details).setOnMenuItemClickListener(mOnMyActionClickListener);
        }


        private MenuItem.OnMenuItemClickListener getMenuItemListener() {
            return new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    mCursor.moveToPosition(getAdapterPosition());
                    switch (item.getItemId()) {
                        case 1:
                            mListener.onDeleteItem(mCursor.getInt(ItemLoader.Query.ID_PODCAST));
                            return true;
                        case 2:
                            mListener.onShowDetail(mCursor);
                            return true;
                        default:
                            return true;
                    }
                }
            };
        }


    }


}
