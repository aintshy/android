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
import android.os.Bundle;
import com.aintshy.android.R;
import com.aintshy.android.api.Talk;
import com.aintshy.android.ui.Swipe;
import com.aintshy.android.ui.history.HistoryActivity;

/**
 * Talk activity.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
public final class TalkActivity extends Activity implements Swipe.Target {

    @Override
    public void onCreate(final Bundle state) {
        super.onCreate(state);
        this.setContentView(R.layout.wait);
    }

    @Override
    public void onStart() {
        super.onStart();
        new Swipe(this).attach(this, R.id.main);
        final int num = this.getIntent().getIntExtra(
            Talk.class.getName(), 0
        );
        if (num == 0) {
            new InboxRedirect(this).execute();
        } else {
            new CreateMessages(this, num).execute();
        }
    }

    @Override
    public void onSwipeRight() {
        this.startActivity(
            new Intent(this, HistoryActivity.class)
        );
    }

    @Override
    public void onSwipeLeft() {
        new Inbox.Locator(this).find().swipe();
        this.startActivity(
            new Intent(this, TalkActivity.class)
        );
    }

}
