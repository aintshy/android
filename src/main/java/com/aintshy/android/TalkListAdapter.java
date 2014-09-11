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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        return Iterables.size(this.talk.messages()) + 1;
    }

    @Override
    public Object getItem(final int position) {
        return "";
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(final int idx, final View view, final ViewGroup grp) {
        final LayoutInflater inflater = LayoutInflater.class.cast(
            this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
        );
        final View row;
        if (idx == 0) {
            row = inflater.inflate(R.layout.talk_head, grp, false);
            final Human human = this.talk.talker();
            TextView.class.cast(row.findViewById(R.id.human)).setText(
                String.format(
                    "%s %d %c", human.name(), human.age(), human.sex()
                )
            );
        } else {
            row = inflater.inflate(R.layout.talk_message, grp, false);
            TextView.class
                .cast(row.findViewById(R.id.text))
                .setText(Iterables.get(this.talk.messages(), idx - 1).text());
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
}

