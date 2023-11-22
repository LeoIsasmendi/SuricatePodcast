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
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import leoisasmendi.android.com.suricatepodcast.R;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchListViewHolder> {

    private final String TAG = getClass().getSimpleName();
    private SearchList mList;
    private SearchList mSelectedList;
    private Context mContext;

    public SearchAdapter(Context context, SearchList aList, SearchList aSelectedList) {
        mContext = context;
        mList = aList;
        mSelectedList = aSelectedList;
    }

    @Override
    public void onBindViewHolder(SearchListViewHolder holder, int position) {
        final PlaylistItem item = mList.get(position);
        holder.item = item;

        Picasso.with(mContext)
                .load(holder.item.getPoster())
                .placeholder(R.drawable.picture)
                .error(R.drawable.picture)
                .into(holder.posterView);

        holder.getNameView().setText(item.getTitle());
        holder.getDurationView().setText(item.getDuration());
        holder.getSelectedView().setChecked(item.getSelected());
        holder.getSelectedView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.toggleSelected();
                if (item.getSelected()) {
                    if (!mSelectedList.contains(item)) {
                        Log.d(TAG, "updateSelectedList: ADDED");
                        mSelectedList.add(item);
                    }
                } else {
                    Log.d(TAG, "updateSelectedList: REMOVED");
                    mSelectedList.remove(item);
                }
            }
        });

    }

    @Override
    public SearchListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false);
        return new SearchListViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    // View Holder
    class SearchListViewHolder extends RecyclerView.ViewHolder {

        public PlaylistItem item;
        private TextView nameView;
        private TextView durationView;
        private CheckBox selectedView;
        private ImageView posterView;

        SearchListViewHolder(View itemView) {
            super(itemView);
            posterView = (ImageView) itemView.findViewById(R.id.search_item_poster);
            nameView = (TextView) itemView.findViewById(R.id.search_item_name);
            durationView = (TextView) itemView.findViewById(R.id.search_item_length);
            selectedView = (CheckBox) itemView.findViewById(R.id.search_item_selected);
        }

        public TextView getNameView() {
            return nameView;
        }

        public TextView getDurationView() {
            return durationView;
        }

        public CheckBox getSelectedView() {
            return selectedView;
        }

    }
}
