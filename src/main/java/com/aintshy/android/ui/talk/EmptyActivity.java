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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;
import com.aintshy.android.R;
import com.aintshy.android.ui.Swipe;
import com.aintshy.android.ui.history.HistoryActivity;
import com.jcabi.aspects.Tv;
import java.util.concurrent.TimeUnit;

/**
 * No new talks for you.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
public final class EmptyActivity extends Activity implements Swipe.Target {

    @Override
    public void onStart() {
        super.onStart();
        this.setContentView(R.layout.talk_empty);
        new Swipe(this).attach(this, R.id.main);
        new Refresh().execute(new Inbox.Locator(this).find());
    }

    @Override
    public void onSwipeRight() {
        this.startActivity(
            new Intent(this, HistoryActivity.class)
        );
        this.finish();
    }

    @Override
    public void onSwipeLeft() {
        this.startActivity(
            new Intent(this, TalkActivity.class)
        );
        this.finish();
    }

    private final class Refresh extends AsyncTask<Inbox, Integer, Boolean> {
        @Override
        public Boolean doInBackground(final Inbox... inbox) {
            boolean found = false;
            int step = 0;
            while (!EmptyActivity.this.isFinishing()) {
                if (step % Tv.TEN == 0
                    && inbox[0].current().iterator().hasNext()) {
                    found = true;
                    break;
                }
                this.publishProgress(step);
                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (final InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException(ex);
                }
                ++step;
            }
            return found;
        }
        @Override
        public void onProgressUpdate(final Integer... secs) {
            TextView.class.cast(EmptyActivity.this.findViewById(R.id.progress))
                .setText(String.format("%d", secs[0]));
        }
        @Override
        public void onPostExecute(final Boolean found) {
            if (found) {
                EmptyActivity.this.startActivity(
                    new Intent(EmptyActivity.this, TalkActivity.class)
                );
            }
        }
    }

}
