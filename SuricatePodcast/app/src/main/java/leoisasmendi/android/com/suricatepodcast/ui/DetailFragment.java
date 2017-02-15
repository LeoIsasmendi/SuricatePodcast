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

package leoisasmendi.android.com.suricatepodcast.ui;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import leoisasmendi.android.com.suricatepodcast.MainActivity;
import leoisasmendi.android.com.suricatepodcast.R;
import leoisasmendi.android.com.suricatepodcast.parcelable.EpisodeParcelable;


public class DetailFragment extends Fragment {

    private EpisodeParcelable mParcelable;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance() {
        DetailFragment fragment = new DetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParcelable = getArguments().getParcelable("EXTRA_EPISODE");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.detail_fragment, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).setActionBarTitle(R.string.detail_fragment_title);
        loadParcelableIntoView();
    }

    private void loadParcelableIntoView() {
        if (mParcelable != null) {
            setTitle(mParcelable.getTitle());
            setDetail(mParcelable.getDetail());
            setDuration(mParcelable.getDuration());
        }
    }

    private void setTitle(String aString) {
        TextView textView = (TextView) getView().findViewById(R.id.detail_name);
        textView.setText(aString);
    }

    private void setDetail(String aString) {
        TextView textView = (TextView) getView().findViewById(R.id.detail_description);
        textView.setText(aString);
    }

    private void setDuration(String aString) {
        TextView textView = (TextView) getView().findViewById(R.id.detail_duration);
        textView.setText(aString);
    }


}