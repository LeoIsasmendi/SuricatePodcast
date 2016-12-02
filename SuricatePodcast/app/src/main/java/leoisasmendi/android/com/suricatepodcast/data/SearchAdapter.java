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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import leoisasmendi.android.com.suricatepodcast.R;
import leoisasmendi.android.com.suricatepodcast.ui.SearchFragment;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchListViewHolder> {

    private List<SearchItem> list;
    private final SearchFragment.OnFragmentInteractionListener mListener;
    private View.OnClickListener mClickListener;

    public SearchAdapter(List<SearchItem> aList, SearchFragment.OnFragmentInteractionListener listener) {
        list = aList;
        mListener = listener;
        mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onAddObjectToPlaylist();
                }
            }
        };
    }

    @Override
    public void onBindViewHolder(SearchListViewHolder holder, int position) {
        holder.getNameView().setText(list.get(position).getName());
        holder.getLengthView().setText(list.get(position).getLength());
        holder.getSelected().setChecked(list.get(position).getSelected());
        holder.getmView().setOnClickListener(mClickListener);
    }

    @Override
    public SearchListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false);
        SearchListViewHolder listViewHolder = new SearchListViewHolder(view);
        return listViewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SearchListViewHolder extends RecyclerView.ViewHolder {
        private TextView nameView;
        private TextView lengthView;
        private CheckBox selected;
        private final View mView;

        SearchListViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            nameView = (TextView) itemView.findViewById(R.id.search_item_name);
            lengthView = (TextView) itemView.findViewById(R.id.search_item_length);
            selected = (CheckBox) itemView.findViewById(R.id.search_item_selected);
        }

        public TextView getNameView() {
            return nameView;
        }

        public TextView getLengthView() {
            return lengthView;
        }

        public CheckBox getSelected() {
            return selected;
        }

        public View getmView() {
            return mView;
        }
    }
}
