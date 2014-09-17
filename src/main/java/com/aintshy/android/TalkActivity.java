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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import com.aintshy.android.api.Hub;
import com.aintshy.android.api.Talk;
import com.google.common.collect.Iterables;
import com.jcabi.aspects.Tv;

/**
 * Talk activity.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
public final class TalkActivity extends Activity {

    @Override
    public void onStart() {
        super.onStart();
        this.setContentView(R.layout.wait);
        final int num = this.getIntent().getIntExtra(Talk.class.getName(), 0);
        if (num == 0) {
            new LoadInbox().execute(
                App.class.cast(this.getApplication()).inbox()
            );
        } else {
            new LoadTalk(
                App.class.cast(this.getApplication()).hub()
            ).execute(num);
        }
    }

    /**
     * Swipe right.
     */
    private void onSwipeRight() {
        this.startActivity(
            new Intent(this, HistoryActivity.class)
        );
    }

    /**
     * Swipe left.
     */
    private void onSwipeLeft() {
        this.startActivity(
            new Intent(this, TalkActivity.class)
        );
    }

    /**
     * Render this talk.
     * @param talk The talk
     */
    private void render(final Talk talk) {
        this.setContentView(R.layout.talk);
        final ListView list = ListView.class.cast(
            this.findViewById(R.id.talks)
        );
        list.setAdapter(new TalkListAdapter(this, talk));
        final GestureDetector detector = new GestureDetector(
            this, new GestureListener()
        );
        list.setOnTouchListener(
            new View.OnTouchListener() {
                @Override
                public boolean onTouch(final View view,
                    final MotionEvent event) {
                    return detector.onTouchEvent(event);
                }
            }
        );
    }

    private final class LoadInbox
        extends AsyncTask<Inbox, Integer, Iterable<Talk>> {
        @Override
        protected Iterable<Talk> doInBackground(final Inbox... inbox) {
            return inbox[0].current();
        }
        @Override
        protected void onPostExecute(final Iterable<Talk> talks) {
            if (Iterables.isEmpty(talks)) {
                TalkActivity.this.startActivity(
                    new Intent(TalkActivity.this, EmptyActivity.class)
                );
            }
            TalkActivity.this.render(Iterables.get(talks, 0));
        }
    }

    private final class LoadTalk extends AsyncTask<Integer, Integer, Talk> {
        private final transient Hub hub;
        private LoadTalk(final Hub hbe) {
            super();
            this.hub = hbe;
        }
        @Override
        protected Talk doInBackground(final Integer... number) {
            return this.hub.talk(number[0]);
        }
        @Override
        protected void onPostExecute(final Talk talk) {
            TalkActivity.this.render(talk);
        }
    }

    private final class GestureListener
        extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(final MotionEvent event) {
            return true;
        }
        @Override
        public boolean onFling(final MotionEvent first,
            final MotionEvent second, final float horiz, final float vert) {
            final float width = second.getX() - first.getX();
            final float height = second.getY() - first.getY();
            final boolean done;
            if (Math.abs(width) > Math.abs(height)
                && Math.abs(width) > (float) Tv.HUNDRED
                && Math.abs(horiz) > (float) Tv.HUNDRED) {
                if (width > 0.0f) {
                    TalkActivity.this.onSwipeRight();
                } else {
                    TalkActivity.this.onSwipeLeft();
                }
                done = true;
            } else {
                done = false;
            }
            return done;
        }
    }

}
