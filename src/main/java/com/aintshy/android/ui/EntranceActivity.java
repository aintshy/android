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
import android.content.Intent;
import android.os.AsyncTask;
import com.aintshy.android.Bag;
import com.aintshy.android.R;
import com.aintshy.android.api.Hub;
import com.aintshy.android.api.NoAuthException;
import com.aintshy.android.cached.CdHub;
import com.aintshy.android.rest.RtEntrance;
import com.aintshy.android.ui.login.ConfirmActivity;
import com.aintshy.android.ui.login.DetailsActivity;
import com.aintshy.android.ui.login.LoginActivity;
import com.aintshy.android.ui.talk.TalkActivity;

/**
 * Entrance activity.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
public final class EntranceActivity extends Activity {

    @Override
    public void onStart() {
        super.onStart();
        this.setContentView(R.layout.wait);
        new AsyncTask<Void, Integer, Void>() {
            @Override
            public Void doInBackground(final Void... none) {
                EntranceActivity.this.enter();
                return null;
            }
        }.execute();
    }

    /**
     * Enter.
     */
    private void enter() {
        try {
            final Hub hub = Bag.class.cast(this.getApplication()).fetch(
                Hub.class,
                new Bag.Source<Hub>() {
                    @Override
                    public Hub create() {
                        return new CdHub(new RtEntrance().auth("", ""));
                    }
                }
            );
            if (hub.profile().myself().age() == 0) {
                this.startActivity(new Intent(this, DetailsActivity.class));
            } else if (!hub.profile().confirmed()) {
                this.startActivity(new Intent(this, ConfirmActivity.class));
            } else {
                this.startActivity(new Intent(this, TalkActivity.class));
            }
        } catch (final NoAuthException ex) {
            this.startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
