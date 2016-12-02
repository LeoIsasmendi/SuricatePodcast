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

package leoisasmendi.android.com.suricatepodcast.model;

public class EpisodeModel {

    private int id;
    private String title;
    private String description;
    //    private Date date_created;
    private String identifier; // URL for source audio file
    private int duration; // Length of audio file in seconds
    private String showId;
    private String showTitle;

    public EpisodeModel() {
    }

    public EpisodeModel(int anId, String aTitle, String aDescription) {
        id = anId;
        title = aTitle;
        description = aDescription;
    }

    public int getId() {
        return id;
    }

    public EpisodeModel setId(int id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public EpisodeModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public EpisodeModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getIdentifier() {
        return identifier;
    }

    public EpisodeModel setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }


}
