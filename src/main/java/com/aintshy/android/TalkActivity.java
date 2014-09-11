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
import com.aintshy.android.api.Talk;
import com.aintshy.android.rest.RtEntrance;
import com.aintshy.android.svc.NextTalk;
import com.jcabi.aspects.Tv;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Talk activity.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
public final class TalkActivity extends Activity implements NextTalk.Consumer {

    /**
     * Next talk provider.
     */
    private transient NextTalk nexter;

    /**
     * Next talks to show.
     */
    private final transient List<Talk> talks = new ArrayList<Talk>(Tv.TWENTY);

    @Override
    public void onCreate(final Bundle state) {
        super.onCreate(state);
        try {
            this.nexter = new NextTalk(
                new RtEntrance().auth("4FHQBF9I-4D5F58JN-7KO2KJQL-0LFKS0HU-7C0GSK3F-99A18IJF-003L0L8V-0K4G2TOA-A1F3GEH7-8G9JOKIM-2174U0PU-4T30461G-E0Q4O91J-3CDLQ7OK-DO======")
            );
        } catch (final IOException ex) {
             throw new IllegalStateException(ex);
        }
        this.nexter.subscribe(this);
        this.setContentView(R.layout.talk);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.show();
    }

    @Override
    public int talksNeeded() {
        return Tv.TWENTY - this.talks.size();
    }

    @Override
    public void seeNextTalk(final Talk talk) {
        this.talks.add(talk);
    }

    /**
     * Show current talk.
     */
    private void show() {
        if (this.talks.isEmpty()) {
            // show spinning wheel
            Log.i(this.getClass().getName(), "nothing to show");
        } else {
            final Talk talk = this.talks.get(0);
            ListView.class.cast(this.findViewById(R.id.talks)).setAdapter(
                new TalkListAdapter(this, talk)
            );
            Log.i(this.getClass().getName(), "talk rendered");
        }
    }

}
