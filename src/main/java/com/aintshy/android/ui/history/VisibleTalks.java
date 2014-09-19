/**
 * Copyright (c) 2014, Aintshy.com
 * All rights reserved.
 *
 * Redistribution and use in source or binary forms, with or without
 * modification, are NOT permitted.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.aintshy.android.ui.history;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.aintshy.android.R;
import com.aintshy.android.api.Talk;
import com.aintshy.android.flat.FtTalk;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * History list adapter.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
final class VisibleTalks implements ListAdapter, UpdateHistory.Target {

    private final transient Context home;
    private final transient ConcurrentMap<Integer, Talk> talks =
        new ConcurrentSkipListMap<Integer, Talk>();
    private final transient AtomicInteger total = new AtomicInteger();
    private final transient DataSetObservable observe = new DataSetObservable();

    /**
     * Ctor.
     * @param ctx Context
     */
    VisibleTalks(final Context ctx) {
        this.home = ctx;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(final int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(final DataSetObserver observer) {
        this.observe.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(final DataSetObserver observer) {
        this.observe.unregisterObserver(observer);
    }

    @Override
    public int getCount() {
        return this.total.get();
    }

    @Override
    public Object getItem(final int position) {
        return position;
    }

    @Override
    public long getItemId(final int idx) {
        return (long) idx;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(final int idx, final View view, final ViewGroup grp) {
        final View row;
        if (view == null) {
            row = LayoutInflater.class.cast(
                this.home.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
            ).inflate(R.layout.history_item, grp, false);
        } else {
            row = view;
        }
        if (this.talks.containsKey(idx)) {
            new AsyncTask<Void, Void, Talk>() {
                @Override
                protected Talk doInBackground(final Void... params) {
                    final Talk talk = VisibleTalks.this.talks.get(idx);
                    Talk flat = null;
                    if (talk != null) {
                        flat = new FtTalk(talk);
                    }
                    return flat;
                }
                @Override
                protected void onPostExecute(final Talk talk) {
                    if (talk != null) {
                        VisibleTalks.this.render(row, talk);
                    }
                }
            }.execute();
        }
        return row;
    }

    @Override
    public int getItemViewType(final int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void update(final Map<Integer, Talk> map, final boolean more) {
        this.talks.putAll(map);
        final int max;
        if (map.isEmpty()) {
            max = -1;
        } else {
            max = Collections.max(map.keySet());
        }
        if (more) {
            this.total.set(max + 2);
        } else {
            this.total.set(max + 1);
        }
        this.observe.notifyChanged();
    }

    /**
     * Render talk by number into given view.
     * @param row View to render into
     * @param talk Talk to render
     */
    private void render(final View row, final Talk talk) {
        row.setTag(talk);
        ImageView.class
            .cast(row.findViewById(R.id.photo))
            .setImageBitmap(talk.role().photo());
        TextView.class
            .cast(row.findViewById(R.id.text))
            .setText(talk.role().name());
        TextView.class
            .cast(row.findViewById(R.id.message))
            .setText(talk.messages().fetch(0, 0).iterator().next().text());
    }

}

