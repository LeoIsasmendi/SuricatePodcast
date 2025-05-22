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
package leoisasmendi.android.com.suricatepodcast.data

import android.database.Cursor
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.OnCreateContextMenuListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import leoisasmendi.android.com.suricatepodcast.R
import leoisasmendi.android.com.suricatepodcast.data.PlaylistCursorAdapter.ViewHolder
import leoisasmendi.android.com.suricatepodcast.ui.MainFragment.OnFragmentInteractionListener
import java.util.Locale

class PlaylistCursorAdapter(private val mListener: OnFragmentInteractionListener) :
    RecyclerViewCursorAdapter<ViewHolder?>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        /*
        val view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.main_playlist_item, parent, false)
        return PlaylistCursorAdapter.ViewHolder(view)
        */
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(
        holder: ViewHolder?,
        cursor: Cursor?
    ) {
        holder?.bindData(cursor!!)
    }


    // ViewHolder class
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, OnCreateContextMenuListener {
        private var mCursor: Cursor? = null
        private val counterView: TextView
        private val showTitleView: TextView
        private val nameView: TextView
        private val durationView: TextView

        private val mOnMyActionClickListener: MenuItem.OnMenuItemClickListener

        init {
            counterView = itemView.findViewById<View?>(R.id.list_item_counter) as TextView
            showTitleView = itemView.findViewById<View?>(R.id.playlist_item_show_name) as TextView
            nameView = itemView.findViewById<View?>(R.id.playlist_item_name) as TextView
            durationView = itemView.findViewById<View?>(R.id.playlist_item_length) as TextView
            mOnMyActionClickListener = this.menuItemListener
            itemView.setOnClickListener(this)
            itemView.setOnCreateContextMenuListener(this)
        }


        fun bindData(cursor: Cursor) {
            mCursor = cursor
            counterView.setText(String.format(Locale.US, "%d.", getAdapterPosition()))
            nameView.setText(mCursor!!.getString(ItemLoader.Query.TITLE))
            showTitleView.setText(mCursor!!.getString(ItemLoader.Query.SHOW_TITLE))
            durationView.setText(mCursor!!.getString(ItemLoader.Query.DURATION))
        }

        override fun onClick(view: View?) {
            mCursor!!.moveToPosition(getAdapterPosition())
            mListener.onClick(getAdapterPosition(), mCursor)
        }

        override fun onCreateContextMenu(
            contextMenu: ContextMenu,
            view: View?,
            contextMenuInfo: ContextMenuInfo?
        ) {
            contextMenu.setHeaderTitle(R.string.context_menu_title)
            contextMenu.add(0, 1, 0, R.string.item_menu_delete)
                .setOnMenuItemClickListener(mOnMyActionClickListener)
            contextMenu.add(0, 2, 0, R.string.item_menu_details)
                .setOnMenuItemClickListener(mOnMyActionClickListener)
        }


        private val menuItemListener: MenuItem.OnMenuItemClickListener
            get() = object : MenuItem.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    mCursor!!.moveToPosition(getAdapterPosition())
                    when (item.getItemId()) {
                        1 -> {
                            mListener.onDeleteItem(mCursor!!.getInt(ItemLoader.Query.ID_PODCAST))
                            return true
                        }

                        2 -> {
                            mListener.onShowDetail(mCursor)
                            return true
                        }

                        else -> return true
                    }
                }
            }
    }
}
