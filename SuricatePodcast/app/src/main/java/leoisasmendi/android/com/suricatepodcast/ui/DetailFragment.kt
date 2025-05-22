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
package leoisasmendi.android.com.suricatepodcast.ui

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import leoisasmendi.android.com.suricatepodcast.R
import leoisasmendi.android.com.suricatepodcast.parcelable.EpisodeParcelable

class DetailFragment : Fragment() {
    private var mParcelable: EpisodeParcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (getArguments() != null) {
            mParcelable = getArguments().getParcelable<EpisodeParcelable?>("EXTRA_EPISODE")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        loadParcelableIntoView()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.menu_item_share).setVisible(true)
        super.onPrepareOptionsMenu(menu)
    }

    private fun loadParcelableIntoView() {
        if (mParcelable != null) {
            setTitle(mParcelable!!.title)
            setDetail(mParcelable!!.detail)
            setDuration(mParcelable!!.duration)
            setPoster(mParcelable!!.poster)
            setDescription(mParcelable!!.description)
        }
    }

    private fun setTitle(aString: String?) {
        val textView = getView()!!.findViewById<View?>(R.id.detail_name) as TextView
        setText(textView, aString, R.string.default_detail_title)
    }

    private fun setDetail(aString: String?) {
        val textView = getView()!!.findViewById<View?>(R.id.detail_description) as TextView
        setText(textView, aString, R.string.default_description_text)
    }

    private fun setDuration(aString: String?) {
        val textView = getView()!!.findViewById<View?>(R.id.detail_duration) as TextView
        setText(textView, aString, R.string.default_detail_duration)
    }

    private fun setText(textView: TextView, aString: String?, resource_id: Int) {
        if (aString != null && !aString.isEmpty()) {
            textView.setText(aString)
        } else {
            textView.setText(resource_id)
        }
    }

    private fun setPoster(aString: String?) {
        val imageView = getView()!!.findViewById<View?>(R.id.detail_poster) as ImageView?
        Picasso.get()
            .load(aString)
            .placeholder(R.drawable.default_poster)
            .error(R.drawable.default_poster)
            .into(imageView)
    }

    private fun setDescription(aString: String?) {
        val textView = getView()!!.findViewById<View?>(R.id.detail_description) as TextView
        setText(textView, aString, R.string.default_description_text)
    }
}