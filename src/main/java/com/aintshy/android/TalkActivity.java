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
import com.aintshy.android.api.Talk;
import com.aintshy.android.svc.NextTalk;
import com.jcabi.aspects.Tv;
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
    private final transient NextTalk nexter = new NextTalk();

    /**
     * Next talks to show.
     */
    private final transient List<Talk> talks = new ArrayList<Talk>(Tv.TWENTY);

    @Override
    public void onCreate(final Bundle state) {
        super.onCreate(state);
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
        } else {
            final Talk talk = this.talks.get(0);
            // draw this talk
        }
    }

}
