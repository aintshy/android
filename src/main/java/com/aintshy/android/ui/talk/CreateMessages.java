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
import android.widget.ListView;
import android.widget.Toast;
import com.aintshy.android.Entrance;
import com.aintshy.android.R;
import com.aintshy.android.api.Hub;
import com.aintshy.android.api.Talk;
import com.aintshy.android.flat.FtTalk;
import com.aintshy.android.ui.Swipe;

/**
 * List create.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
final class CreateMessages extends
    AsyncTask<Void, Integer, Talk> implements VisibleMessages.Target {

    private final transient Activity home;
    private final transient int number;

    /**
     * Ctor.
     * @param activity Home activity
     * @param num Number
     */
    CreateMessages(final Activity activity, final int num) {
        super();
        this.home = activity;
        this.number = num;
    }

    @Override
    public Talk doInBackground(final Void... none) {
        return new FtTalk(new Entrance(this.home).hub().talk(this.number));
    }

    @Override
    public void onPostExecute(final Talk talk) {
        this.home.setContentView(R.layout.talk_main);
        new Swipe(Swipe.Target.class.cast(this.home)).attach(this.home, R.id.messages);
        final ListView list = ListView.class.cast(
            this.home.findViewById(R.id.messages)
        );
        list.setAdapter(new VisibleMessages(this.home, talk, this));
        new UpdateMessages(this.home, this.number, 0, 20).execute();
    }

    @Override
    public void onAnswer(final String text) {
        final Hub hub = new Entrance(this.home).hub();
        new AsyncTask<Void, Integer, Void>() {
            @Override
            public Void doInBackground(final Void... none) {
                hub.talk(CreateMessages.this.number).post(text);
                new Inbox.Locator(CreateMessages.this.home).find().swipe();
                return null;
            }
            @Override
            public void onPostExecute(final Void none) {
                Toast.makeText(
                    CreateMessages.this.home,
                    "posted!", Toast.LENGTH_SHORT
                ).show();
                CreateMessages.this.home.startActivity(
                    new Intent(CreateMessages.this.home, TalkActivity.class)
                );
            }
        }.execute();
    }

}
