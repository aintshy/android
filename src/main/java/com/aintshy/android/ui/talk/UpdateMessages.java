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
import android.os.AsyncTask;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.aintshy.android.Entrance;
import com.aintshy.android.R;
import com.aintshy.android.api.Hub;
import com.aintshy.android.api.Message;
import com.aintshy.android.api.Talk;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * List update.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
final class UpdateMessages extends AsyncTask<Void, Integer, Map<Integer, Message>> {

    private final transient Activity home;
    private final transient int number;
    private final transient int first;
    private final transient int last;

    /**
     * Ctor.
     * @param activity Home activity
     * @param num Talk number
     * @param start First message to show
     * @param end Last message to show (if possible)
     */
    UpdateMessages(final Activity activity, final int num,
        final int start, final int end) {
        super();
        this.home = activity;
        this.number = num;
        this.first = start;
        this.last = end;
    }

    @Override
    public Map<Integer, Message> doInBackground(final Void... none) {
        final Hub hub = new Entrance(this.home).hub();
        final Talk talk = hub.talk(this.number);
        final Collection<Message> msgs = talk.messages().fetch(this.first, this.last);
        final Map<Integer, Message> map = new HashMap<Integer, Message>(msgs.size());
        int idx = this.first;
        for (final Message msg : msgs) {
            map.put(idx, msg);
            ++idx;
        }
        return map;
    }

    @Override
    public void onPostExecute(final Map<Integer, Message> messages) {
        final ListView list = ListView.class.cast(
            this.home.findViewById(R.id.messages)
        );
        final ListAdapter adapter = list.getAdapter();
        final int max = Collections.max(messages.keySet());
        UpdateMessages.Target.class.cast(adapter).update(
            messages, max >= this.last
        );
    }

    public interface Target {
        /**
         * Update messages.
         * @param msgs Map of them
         * @param more Do we have more?
         */
        void update(Map<Integer, Message> msgs, boolean more);
    }

}
