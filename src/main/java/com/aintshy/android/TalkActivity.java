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
import android.widget.ListView;
import com.aintshy.android.api.Talk;
import com.google.common.collect.Iterables;

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
        new Reload().execute(
            App.class.cast(this.getApplication()).inbox()
        );
    }

    private final class Reload extends AsyncTask<Inbox, Integer, Iterable<Talk>> {
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
            TalkActivity.this.setContentView(R.layout.talk);
            final Talk talk = Iterables.get(talks, 0);
            ListView.class.cast(TalkActivity.this.findViewById(R.id.talks)).setAdapter(
                new TalkListAdapter(TalkActivity.this, talk)
            );
        }
    }

}
