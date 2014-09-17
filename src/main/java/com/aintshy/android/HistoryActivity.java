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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import com.jcabi.aspects.Tv;

/**
 * History activity.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
public final class HistoryActivity extends Activity {

    @Override
    public void onStart() {
        super.onStart();
        this.setContentView(R.layout.history);
        final ListView list = ListView.class.cast(this.findViewById(R.id.talks));
        list.setAdapter(
            new HistoryListAdapter(
                this,
                App.class.cast(this.getApplication()).history()
            )
        );
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

    /**
     * Swipe left.
     */
    private void onSwipeLeft() {
        this.startActivity(
            new Intent(this, TalkActivity.class)
        );
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
                if (width < 0.0f) {
                    HistoryActivity.this.onSwipeLeft();
                }
                done = true;
            } else {
                done = false;
            }
            return done;
        }
    }

}
