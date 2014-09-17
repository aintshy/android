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

import com.aintshy.android.api.Hub;
import com.aintshy.android.api.Talk;
import com.google.common.collect.Iterables;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Inbox with talks.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
final class Inbox {

    /**
     * Hub to use.
     */
    private final transient Hub hub;

    /**
     * Current talk.
     */
    private final transient AtomicReference<Iterable<Talk>> current =
        new AtomicReference<Iterable<Talk>>();

    /**
     * Ctor.
     * @param hbe Hub to work with
     */
    Inbox(final Hub hbe) {
        this.hub = hbe;
    }

    /**
     * Get current talk or empty list if nothing is active now.
     * @return Talks
     */
    public Iterable<Talk> current() {
        final Iterable<Talk> talks;
        if (this.current.get() == null) {
            talks = this.hub.next();
            if (!Iterables.isEmpty(talks)) {
                this.current.set(talks);
            }
        } else {
            talks = this.current.get();
        }
        return talks;
    }

    /**
     * Swipe, to see the next talk.
     */
    public void swipe() {
        this.current.set(null);
    }

}