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
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewCursorAdapter<VH : RecyclerView.ViewHolder?>
    : RecyclerView.Adapter<VH?>() {
    var cursor: Cursor? = null
        private set

    fun swapCursor(cursor: Cursor?) {
        this.cursor = cursor
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (this.cursor != null)
            this.cursor!!.getCount()
        else
            0
    }

    fun getItem(position: Int): Cursor? {
        if (this.cursor != null && !this.cursor!!.isClosed()) {
            this.cursor!!.moveToPosition(position)
        }

        return this.cursor
    }

    override fun onBindViewHolder(holder: VH & Any, position: Int) {
        val cursor = this.getItem(position)
        this.onBindViewHolder(holder, cursor)
    }

    abstract fun onBindViewHolder(holder: VH?, cursor: Cursor?)
}
