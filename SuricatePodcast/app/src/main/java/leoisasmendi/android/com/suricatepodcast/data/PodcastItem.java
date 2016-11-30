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

public class PodcastItem {
    private String name;
    private String length;
    private int id;

    public PodcastItem(int anId, String aName, String aLength) {
        id = anId;
        name = aName;
        length = aLength;
    }

    public String getName() {
        return name;
    }

    public String getLength() {
        return length;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

//    private List<PodcastItem> podcastPlaylist;
//
//    // This method creates an ArrayList that has three Person objects
//// Checkout the project associated with this tutorial on Github if
//// you want to use the same images.
//    private void initializeData() {
//        podcastPlaylist = new ArrayList<>();
//        podcastPlaylist.add(new PodcastItem(1, "Emma Wilson", "3s"));
//        podcastPlaylist.add(new PodcastItem(2, "Lavery Maiss", "25 years old"));
//        podcastPlaylist.add(new PodcastItem(3, "Lillie Watts", "35 years old"));
//    }
