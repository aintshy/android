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
package com.aintshy.android.ui.talk;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.aintshy.android.R;
import com.aintshy.android.api.Human;
import com.aintshy.android.api.Message;
import com.aintshy.android.api.Talk;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Talks for a list view.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
final class VisibleMessages implements ListAdapter, UpdateMessages.Target {

    private final transient Context home;
    private final transient Talk talk;
    private final transient ConcurrentMap<Integer, Message> messages =
        new ConcurrentSkipListMap<Integer, Message>();
    private final transient AtomicInteger total = new AtomicInteger();
    private final transient DataSetObservable observe = new DataSetObservable();
    private final transient VisibleMessages.Target target;

    /**
     * Ctor.
     * @param ctx Activity context
     * @param tlk Talk
     * @param tgt Target for answering
     */
    VisibleMessages(final Context ctx, final Talk tlk,
        final VisibleMessages.Target tgt) {
        this.home = ctx;
        this.talk = tlk;
        this.target = tgt;
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
        this.observe.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(final DataSetObserver observer) {
        this.observe.unregisterObserver(observer);
    }

    @Override
    public int getCount() {
        return this.total.get() + 1;
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
        if (idx == 0) {
            row = this.header(view, grp);
        } else {
            row = this.message(idx - 1, view, grp);
        }
        return row;
    }

    @Override
    public int getItemViewType(final int idx) {
        final int type;
        if (idx == 0) {
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

    @Override
    public void update(final Map<Integer, Message> map, final boolean more) {
        this.messages.putAll(map);
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
     * Render header.
     * @param view View or NULL
     * @param grp Group
     * @return Row view
     */
    private View header(final View view, final ViewGroup grp) {
        final View row;
        if (view == null) {
            row = LayoutInflater.class.cast(
                this.home.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
            ).inflate(R.layout.talk_head, grp, false);
            final Human human = this.talk.role();
            ImageView.class
                .cast(row.findViewById(R.id.photo))
                .setImageBitmap(human.photo());
            TextView.class
                .cast(row.findViewById(R.id.human))
                .setText(
                    String.format(
                        "%s %d %c", human.name(),
                        human.age(), human.sex()
                    )
                );
            final EditText edit = EditText.class.cast(
                row.findViewById(R.id.answer)
            );
            edit.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(final TextView viw,
                        final int action, final KeyEvent event) {
                        boolean handled = false;
                        if (action == EditorInfo.IME_ACTION_SEND) {
                            VisibleMessages.this.target.onAnswer(
                                edit.getText().toString()
                            );
                            handled = true;
                        }
                        return handled;
                    }
                }
            );
        } else {
            row = view;
        }
        return row;
    }

    /**
     * Render message.
     * @param idx Position of the message
     * @param view View or NULL
     * @param grp Group
     * @return Row view
     */
    private View message(final int idx, final View view, final ViewGroup grp) {
        final View row;
        if (view == null) {
            row = LayoutInflater.class.cast(
                this.home.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
            ).inflate(R.layout.talk_message, grp, false);
        } else {
            row = view;
        }
        final TextView label = TextView.class.cast(
            row.findViewById(R.id.text)
        );
        final Message msg = this.messages.get(idx);
        if (msg == null) {
            label.setText("...wait...");
        } else {
            label.setText(msg.text());
            final RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                );
            if (msg.mine()) {
                label.setBackgroundResource(R.color.my_message);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            } else {
                label.setBackgroundResource(R.color.his_message);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }
            label.setLayoutParams(params);
        }
        return row;
    }

    /**
     * Target of answering.
     */
    public interface Target {
        /**
         * Post an onAnswer.
         * @param text Text of it
         */
        void onAnswer(String text);
    }

}

