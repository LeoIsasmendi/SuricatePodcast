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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import leoisasmendi.android.com.suricatepodcast.R;
import leoisasmendi.android.com.suricatepodcast.ui.SearchFragment;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchListViewHolder> {

    private SearchList mList;
    private SearchFragment.OnFragmentInteractionListener mListener;
    private Context mContext;

    public SearchAdapter(Context context, SearchList aList, SearchFragment.OnFragmentInteractionListener listener) {
        mContext = context;
        mList = aList;
        mListener = listener;
    }

    @Override
    public void onBindViewHolder(SearchListViewHolder holder, int position) {
        final SearchItem item = mList.get(position);
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
                if (null != mListener) {
                    mListener.updateSelectedList(item);
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
        return mList.size();
    }

    // View Holder
    class SearchListViewHolder extends RecyclerView.ViewHolder {

        private final View view;
        public SearchItem item;

        private TextView nameView;
        private TextView durationView;
        private CheckBox selectedView;
        private ImageView posterView;

        SearchListViewHolder(View itemView) {
            super(itemView);
            view = itemView;
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

        public View getView() {
            return view;
        }

    }
}
