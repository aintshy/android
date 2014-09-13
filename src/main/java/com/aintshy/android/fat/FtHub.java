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
package com.aintshy.android.fat;

import com.aintshy.android.api.Hub;
import com.aintshy.android.api.Profile;
import com.aintshy.android.api.Talk;
import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Fat hub.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
public final class FtHub implements Hub {

    /**
     * Original hub.
     */
    private final transient Hub origin;

    /**
     * Next talks.
     */
    private final transient Queue<Talk> next =
        new ConcurrentLinkedQueue<Talk>();

    /**
     * Loading right now.
     */
    private final transient AtomicBoolean fetching = new AtomicBoolean();

    /**
     * Ctor.
     * @param hub Original
     */
    FtHub(final Hub hub) {
        this.origin = hub;
    }

    @Override
    public Profile profile() {
        throw new UnsupportedOperationException("#profile()");
    }

    @Override
    public FtItems<Talk> next() {
        final FtItems<Talk> items;
        final Talk talk = this.next.poll();
        if (talk == null) {
            this.fetch();
            items = new FtItems.Loading<Talk>();
        } else {
            items = new FtItems.Solid<Talk>(Collections.singleton(talk));
        }
        return items;
    }

    @Override
    public void ask(final String text) {
        throw new UnsupportedOperationException("#ask()");
    }

    @Override
    public Iterable<Talk> history() {
        throw new UnsupportedOperationException("#history()");
    }

    /**
     * Fetch new talk.
     */
    private void fetch() {
        if (this.fetching.compareAndSet(false, true)) {
            new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        for (final Talk talk : FtHub.this.origin.next()) {
                            FtHub.this.next.add(new FtTalk(talk));
                        }
                        FtHub.this.fetching.set(false);
                    }
                }
            ).start();
        }
    }

}
