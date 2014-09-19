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
package com.aintshy.android.ui;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.jcabi.aspects.Tv;

/**
 * Swipe.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
public final class Swipe extends GestureDetector.SimpleOnGestureListener {

    /**
     * Target.
     */
    private final transient Swipe.Target target;

    /**
     * Ctor.
     * @param tgt Target
     */
    public Swipe(final Swipe.Target tgt) {
        super();
        this.target = tgt;
    }

    @Override
    public boolean onDown(final MotionEvent event) {
        return true;
    }

    @Override
    public boolean onFling(final MotionEvent first,
        final MotionEvent second, final float horiz, final float vert) {
        final boolean done;
        if (first == null || second == null) {
            done = true;
        } else {
            final float width = second.getX() - first.getX();
            final float height = second.getY() - first.getY();
            if (Math.abs(width) > Math.abs(height)
                && Math.abs(width) > (float) Tv.HUNDRED
                && Math.abs(horiz) > (float) Tv.HUNDRED) {
                if (width > 0.0f) {
                    this.target.onSwipeRight();
                } else {
                    this.target.onSwipeLeft();
                }
                done = true;
            } else {
                done = false;
            }
        }
        return done;
    }

    /**
     * Attach it to the activity.
     * @param home Activity
     * @param view View ID to attach it to
     */
    public void attach(final Activity home, final int view) {
        final GestureDetector detector = new GestureDetector(home, this);
        home.findViewById(view).setOnTouchListener(
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
     * Target of events.
     */
    public interface Target {
        /**
         * Swipe right.
         */
        void onSwipeRight();
        /**
         * Swipe left.
         */
        void onSwipeLeft();
    }

}
