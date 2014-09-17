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
package com.aintshy.android;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.aintshy.android.api.History;
import com.aintshy.android.api.Talk;
import com.aintshy.android.flat.FtTalk;
import com.google.common.collect.Iterables;

/**
 * History list adapter.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
final class HistoryListAdapter implements ListAdapter {

    private final transient Context context;
    private final transient History history;

    /**
     * Ctor.
     * @param ctx Context
     * @param hst History
     */
    HistoryListAdapter(final Context ctx, final History hst) {
        this.context = ctx;
        this.history = hst;
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
        //
    }

    @Override
    public void unregisterDataSetObserver(final DataSetObserver observer) {
        //
    }

    @Override
    public int getCount() {
        return 5;
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
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
            ).inflate(R.layout.history_item, grp, false);
        } else {
            row = view;
        }
        new AsyncTask<Void, Void, Talk>() {
            @Override
            protected Talk doInBackground(final Void... params) {
                return new FtTalk(
                    Iterables.get(HistoryListAdapter.this.history.talks(), idx)
                );
            }
            @Override
            protected void onPostExecute(final Talk talk) {
                ImageView.class
                    .cast(row.findViewById(R.id.photo))
                    .setImageBitmap(talk.role().photo());
                TextView.class
                    .cast(row.findViewById(R.id.text))
                    .setText(talk.role().name());
                TextView.class
                    .cast(row.findViewById(R.id.message))
                    .setText(talk.messages().iterator().next().text());
            }
        }.execute();
        return row;
    }

    @Override
    public int getItemViewType(final int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return this.getCount();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

}

