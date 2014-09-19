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
package com.aintshy.android.ui.history;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.aintshy.android.R;
import com.aintshy.android.api.Roll;
import com.aintshy.android.api.Talk;
import com.aintshy.android.ui.Swipe;
import com.aintshy.android.ui.talk.TalkActivity;

/**
 * History activity.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
public final class HistoryActivity extends Activity implements Swipe.Target {

    @Override
    public void onStart() {
        super.onStart();
        this.setContentView(R.layout.wait);
        new Swipe(this).attach(this, R.id.main);
        new UpdateHistory(this, 0, 10).execute();
    }

    /**
     * On item click.
     * @param talk Talk just clicked
     */
    private void onClick(final Talk talk) {
        final Intent intent = new Intent(this, TalkActivity.class);
        intent.putExtra(Talk.class.getName(), talk.number());
        this.startActivity(intent);
    }

    @Override
    public void onSwipeLeft() {
        this.startActivity(
            new Intent(this, TalkActivity.class)
        );
    }

    @Override
    public void onSwipeRight() {
        // nothing to do
    }

    /**
     * Render the list.
     * @param size Size of the history_main to start with
     */
    private void render(final Roll<Talk> history, final int size) {
        this.setContentView(R.layout.history_main);
        final ListView list = ListView.class.cast(this.findViewById(R.id.talks));
        list.setAdapter(new VisibleTalks(this));
        list.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(final AdapterView<?> parent,
                    final View view, final int position, final long idx) {
                    HistoryActivity.this.onClick(
                        Talk.class.cast(view.getTag())
                    );
                }
            }
        );
    }

}
