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
import android.widget.TextView;
import com.aintshy.android.Entrance;
import com.aintshy.android.R;
import com.aintshy.android.api.Hub;
import com.aintshy.android.api.Talk;
import com.aintshy.android.flat.FtTalk;
import com.aintshy.android.ui.Swipe;
import com.aintshy.android.ui.talk.TalkActivity;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Update history.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
final class UpdateHistory extends AsyncTask<Void, Integer, Map<Integer, Talk>> {

    /**
     * Activity.
     */
    private final transient Activity home;

    /**
     * First talk to show.
     */
    private final transient int first;

    /**
     * Last talk to show.
     */
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
    public void onPreExecute() {
        TextView.class.cast(this.home.findViewById(R.id.progress))
            .setText("loading...");
    }

    @Override
    public Map<Integer, Talk> doInBackground(final Void... none) {
        final Hub hub = new Entrance(this.home).hub();
        final Collection<Talk> talks =
            hub.history().fetch(this.first, this.last);
        final ConcurrentMap<Integer, Talk> map =
            new ConcurrentHashMap<Integer, Talk>(talks.size());
        int idx = this.first;
        for (final Talk talk : talks) {
            map.put(idx, UpdateHistory.flatten(talk));
            this.publishProgress(idx + 1, talks.size());
            ++idx;
        }
        return map;
    }

    @Override
    public void onProgressUpdate(final Integer... idx) {
        TextView.class.cast(this.home.findViewById(R.id.progress))
            .setText(String.format("%d/%d", idx[0], idx[1]));
    }

    @Override
    public void onPostExecute(final Map<Integer, Talk> talks) {
        this.home.setContentView(R.layout.history_main);
        new Swipe(Swipe.Target.class.cast(this.home))
            .attach(this.home, R.id.talks);
        final ListView list = ListView.class.cast(
            this.home.findViewById(R.id.talks)
        );
        ListAdapter adapter = list.getAdapter();
        if (adapter == null) {
            adapter = new VisibleTalks(this.home);
            list.setAdapter(adapter);
            list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    // @checkstyle ParameterNumberCheck (6 lines)
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
        UpdateHistory.OnClick.class.cast(adapter).update(
            talks, max >= this.last
        );
    }

    /**
     * Flatten one talk.
     * @param talk The talk
     * @return Flat one
     */
    private static Talk flatten(final Talk talk) {
        return new FtTalk(talk);
    }

    public interface OnClick {
        /**
         * Update talks.
         * @param talks Map of them
         * @param more Do we have more?
         */
        void update(Map<Integer, Talk> talks, boolean more);
    }

}
