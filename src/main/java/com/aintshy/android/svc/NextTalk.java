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
package com.aintshy.android.svc;

import com.aintshy.android.api.Hub;
import com.aintshy.android.api.Talk;

/**
 * Fetch next talk from hub.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
public final class NextTalk {

    /**
     * Hub to work with.
     */
    private final transient Hub hub;

    /**
     * Ctor.
     * @param hbe Hub
     */
    public NextTalk(final Hub hbe) {
        this.hub = hbe;
    }

    /**
     * Send me next talks when you have them.
     * @param consumer The consumer
     */
    public void subscribe(NextTalk.Consumer consumer) {
        // to do
        consumer.seeNextTalk(this.hub.next().iterator().next());
    }

    /**
     * Consumer of talks.
     */
    public interface Consumer {
        /**
         * How many talks do you still need?
         * @return Number of talks you still want
         */
        int talksNeeded();
        /**
         * Next talk to show.
         * @param talk The talk
         */
        void seeNextTalk(Talk talk);
    }

}
