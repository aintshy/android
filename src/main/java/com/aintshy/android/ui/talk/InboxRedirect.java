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
import com.aintshy.android.api.Talk;
import com.google.common.collect.Iterables;

/**
 * Redirect via inbox.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
final class InboxRedirect extends AsyncTask<Void, Integer, Iterable<Talk>> {

    /**
     * Home activity.
     */
    private final transient Activity home;

    /**
     * Ctor.
     * @param activity Home activity
     */
    InboxRedirect(final Activity activity) {
        super();
        this.home = activity;
    }

    @Override
    public Iterable<Talk> doInBackground(final Void... none) {
        return new Inbox.Locator(this.home).find().current();
    }

    @Override
    public void onPostExecute(final Iterable<Talk> talks) {
        if (Iterables.isEmpty(talks)) {
            this.home.startActivity(
                new Intent(this.home, EmptyActivity.class)
            );
        } else {
            final Talk talk = Iterables.get(talks, 0);
            final Intent intent = new Intent(this.home, TalkActivity.class);
            intent.putExtra(Talk.class.getName(), talk.number());
            this.home.startActivity(intent);
        }
    }

}
