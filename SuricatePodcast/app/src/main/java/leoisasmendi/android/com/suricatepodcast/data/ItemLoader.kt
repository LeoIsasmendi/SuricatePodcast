/*
 * The MIT License (MIT)
 * Copyright (c) 2017. Sergio Leonardo Isasmendi
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
package leoisasmendi.android.com.suricatepodcast.data

import android.content.Context
import android.content.CursorLoader
import android.net.Uri

/**
 * Helper for loading a list of items or a single item.
 */
class ItemLoader(
    context: Context?,
    uri: Uri?,
    projection: Array<String?>?,
    selection: String?,
    selectionArgs: Array<String?>?,
    sortOrder: String?
) : CursorLoader(context, uri, projection, selection, selectionArgs, sortOrder) {
    interface Query {
        companion object {
            val PROJECTION: Array<String?> = arrayOf<String?>(
                PodcastContract.PodcastEntry._ID.toString(),
                PodcastContract.PodcastEntry.COLUMN_ID,
                PodcastContract.PodcastEntry.COLUMN_TITLE,
                PodcastContract.PodcastEntry.COLUMN_SHOW_TITLE,
                PodcastContract.PodcastEntry.COLUMN_DURATION,
                PodcastContract.PodcastEntry.COLUMN_AUDIO,
                PodcastContract.PodcastEntry.COLUMN_POSTER,
                PodcastContract.PodcastEntry.COLUMN_DESCRIPTION
            )

            const val ID_PODCAST: Int = 1
            const val TITLE: Int = 2
            const val SHOW_TITLE: Int = 3
            const val DURATION: Int = 4
            const val AUDIO: Int = 5
            const val POSTER: Int = 6
            const val DESCRIPTION: Int = 7
        }
    }
}
