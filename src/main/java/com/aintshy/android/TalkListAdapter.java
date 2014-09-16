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
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.aintshy.android.api.Human;
import com.aintshy.android.api.Talk;
import com.google.common.collect.Iterables;

/**
 * Talk list adapter.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
final class TalkListAdapter implements ListAdapter {

    private final transient Context context;
    private final transient Talk talk;

    /**
     * Ctor.
     * @param ctx Context
     * @param tlk Talk
     */
    TalkListAdapter(final Context ctx, final Talk tlk) {
        this.context = ctx;
        this.talk = tlk;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(final int position) {
        return false;
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
        return 10;
    }

    @Override
    public Object getItem(final int position) {
        return "";
    }

    @Override
    public long getItemId(final int idx) {
        return idx;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(final int idx, final View view, final ViewGroup grp) {
        final View row;
        if (idx == 0) {
            row = this.header(view, grp);
        } else {
            row = this.message(idx, view, grp);
        }
        return row;
    }

    @Override
    public int getItemViewType(final int position) {
        final int type;
        if (position == 0) {
            type = 0;
        } else {
            type = 1;
        }
        return type;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * Render header.
     * @param view View or NULL
     * @param grp Group
     * @return Row view
     */
    private View header(final View view, final ViewGroup grp) {
        final View row;
        if (view == null) {
            row = LayoutInflater.class.cast(
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
            ).inflate(R.layout.talk_head, grp, false);
            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected void onPreExecute() {
                    // set empty image
                }
                @Override
                protected Bitmap doInBackground(final Void... params) {
                    return TalkListAdapter.this.talk.role().photo();
                }
                @Override
                protected void onPostExecute(final Bitmap photo) {
                    ImageView.class
                        .cast(row.findViewById(R.id.photo))
                        .setImageBitmap(photo);
                }
            }.execute();
            final TextView label = TextView.class.cast(row.findViewById(R.id.human));
            new AsyncTask<Void, Void, String>() {
                @Override
                protected void onPreExecute() {
                    label.setText("...wait...");
                }
                @Override
                protected String doInBackground(final Void... params) {
                    final Human human = TalkListAdapter.this.talk.role();
                    return String.format(
                        "%s %d %c", human.name(), human.age(), human.sex()
                    );
                }
                @Override
                protected void onPostExecute(final String name) {
                    label.setText(name);
                }
            }.execute();
        } else {
            row = view;
        }
        return row;
    }

    /**
     * Render header.
     * @param idx Position of the message
     * @param view View or NULL
     * @param grp Group
     * @return Row view
     */
    private View message(final int idx, final View view, final ViewGroup grp) {
        final View row;
        if (view == null) {
            row = LayoutInflater.class.cast(
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
            ).inflate(R.layout.talk_message, grp, false);
        } else {
            row = view;
        }
        final TextView label = TextView.class.cast(row.findViewById(R.id.text));
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                label.setText("...wait...");
            }
            @Override
            protected String doInBackground(final Void... params) {
                return Iterables.get(
                    TalkListAdapter.this.talk.messages(), idx - 1
                ).text();
            }
            @Override
            protected void onPostExecute(final String text) {
                label.setText(text);
            }
        }.execute();
        return row;
    }

}

