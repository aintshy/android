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
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import com.aintshy.android.api.Hub;
import com.aintshy.android.api.Talk;
import com.aintshy.android.fat.FtHub;
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
    public void onCreate(final Bundle state) {
        super.onCreate(state);
        this.setContentView(R.layout.talk);
        final Hub hub = App.class.cast(this.getApplication()).hub();
        final Iterable<Talk> next = hub.next();
        if (FtHub.LOADING.equals(next)) {
            // show spinning wheel
            Log.i(this.getClass().getName(), "nothing to show");
        } else {
            final Talk talk = Iterables.get(next, 0);
            ListView.class.cast(this.findViewById(R.id.talks)).setAdapter(
                new TalkListAdapter(this, talk)
            );
            Log.i(this.getClass().getName(), "talk rendered");
        }
    }

}
