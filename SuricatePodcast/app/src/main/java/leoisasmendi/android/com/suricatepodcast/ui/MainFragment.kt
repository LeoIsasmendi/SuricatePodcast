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


import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import leoisasmendi.android.com.suricatepodcast.R
import leoisasmendi.android.com.suricatepodcast.data.PlaylistCursorAdapter
import leoisasmendi.android.com.suricatepodcast.provider.DataProvider

class MainFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor?> {
    /*local*/
    var mListener: OnFragmentInteractionListener? = null
    val TAG: String = javaClass.simpleName
    var mLayoutManager: RecyclerView.LayoutManager? = null
    var mRecyclerView: RecyclerView? = null
    var mAdapter: PlaylistCursorAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        checkListenerImplementation(view.context)

        mRecyclerView = view.findViewById<View?>(R.id.master_fragment) as RecyclerView
        mLayoutManager = LinearLayoutManager(activity)
        mRecyclerView!!.setHasFixedSize(true)
        mRecyclerView!!.setLayoutManager(mLayoutManager)
        mAdapter = PlaylistCursorAdapter(mListener!!)
        mRecyclerView!!.setAdapter(mAdapter)
        setupFAB()
        return view
    }

    private fun setupFAB() {
        val fab = activity?.findViewById<View?>(R.id.contextual_fab) as FloatingActionButton
        fab.setImageResource(R.drawable.magnifier)
        fab.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                mListener!!.searchPodcast()
            }
        }
        )
        fab.contentDescription = getString(R.string.cd_search_fab)
    }

    private fun checkListenerImplementation(context: Context) {
        if (context is OnFragmentInteractionListener) {
            mListener = context as OnFragmentInteractionListener
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnMainListInteractionListener"
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        checkListenerImplementation(context)
    }

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<Cursor?> {
        Log.d(TAG, "onCreateLoader: ")
        return CursorLoader(
            activity?.applicationContext!!,
            DataProvider.CONTENT_URI,
            null,
            null,
            null,
            null
        )
    }

    override fun onLoadFinished(
        loader: Loader<Cursor?>,
        data: Cursor?
    ) {
        Log.d(TAG, "onLoadFinished: ")
        if (mAdapter != null) {
            mAdapter!!.swapCursor(data)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor?>) {
        TODO("Not yet implemented")
    }

    interface OnFragmentInteractionListener {
        fun searchPodcast()

        fun onClick(position: Int, item: Cursor?)

        fun onDeleteItem(itemId: Int)

        fun onShowDetail(item: Cursor?)
    }
}
