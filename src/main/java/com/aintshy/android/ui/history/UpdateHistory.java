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
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.aintshy.android.Bag;
import com.aintshy.android.R;
import com.aintshy.android.api.Hub;
import com.aintshy.android.api.Talk;
import com.aintshy.android.ui.Swipe;
import com.aintshy.android.ui.talk.TalkActivity;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Update history.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
final class UpdateHistory extends AsyncTask<Void, Integer, Map<Integer, Talk>> {

    private final transient Activity home;
    private final transient int first;
    private final transient int last;

    /**
     * Ctor.
     * @param activity Home activity
     * @param start First message to show
     * @param end Last message to show (if possible)
     */
    UpdateHistory(final Activity activity, final int start, final int end) {
        super();
        this.home = activity;
        this.first = start;
        this.last = end;
    }

    @Override
    public Map<Integer, Talk> doInBackground(final Void... none) {
        final Bag bag = Bag.class.cast(this.home.getApplicationContext());
        final Hub hub = bag.fetch(Hub.class, new Bag.Source.Empty<Hub>());
        final Collection<Talk> talks = hub.history().fetch(this.first, this.last);
        final Map<Integer, Talk> map = new HashMap<Integer, Talk>(talks.size());
        int idx = this.first;
        for (final Talk talk : talks) {
            map.put(idx, talk);
            ++idx;
        }
        return map;
    }

    @Override
    public void onPostExecute(final Map<Integer, Talk> talks) {
        this.home.setContentView(R.layout.history_main);
        new Swipe(Swipe.Target.class.cast(this.home)).attach(this.home, R.id.talks);
        final ListView list = ListView.class.cast(
            this.home.findViewById(R.id.talks)
        );
        ListAdapter adapter = list.getAdapter();
        if (adapter == null) {
            adapter = new VisibleTalks(this.home);
            list.setAdapter(adapter);
            list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(final AdapterView<?> parent,
                        final View view, final int position, final long idx) {
                        final Intent intent = new Intent(
                            UpdateHistory.this.home, TalkActivity.class
                        );
                        if (view.getTag() != null) {
                            intent.putExtra(
                                Talk.class.getName(),
                                Talk.class.cast(view.getTag()).number()
                            );
                            UpdateHistory.this.home.startActivity(intent);
                        }
                    }
                }
            );
        }
        final int max = Collections.max(talks.keySet());
        UpdateHistory.Target.class.cast(adapter).update(
            talks, max < this.last
        );
    }

    public interface Target {
        /**
         * Update talks.
         * @param talks Map of them
         * @param more Do we have more?
         */
        void update(Map<Integer, Talk> talks, boolean more);
    }

}
