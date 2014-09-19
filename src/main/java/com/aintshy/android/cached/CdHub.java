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
package com.aintshy.android.cached;

import com.aintshy.android.api.Hub;
import com.aintshy.android.api.Profile;
import com.aintshy.android.api.Roll;
import com.aintshy.android.api.Talk;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import lombok.EqualsAndHashCode;

/**
 * Cached Hub.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@EqualsAndHashCode(of = "origin")
public final class CdHub implements Hub {

    /**
     * Original object.
     */
    private final transient Hub origin;

    /**
     * History.
     */
    private final transient Atomic<Roll<Talk>> hst =
        new Atomic.Default<Roll<Talk>>();

    /**
     * LRU cache.
     */
    private final transient Lru<Talk> lru = new Lru.Talks();

    /**
     * Ctor.
     * @param orgn Original
     */
    public CdHub(final Hub orgn) {
        this.origin = orgn;
    }

    @Override
    public Profile profile() {
        return this.origin.profile();
    }

    @Override
    public Iterable<Talk> next() {
        return Iterables.transform(
            this.origin.next(),
            new Function<Talk, Talk>() {
                @Override
                public Talk apply(final Talk talk) {
                    return CdHub.this.lru.cache(talk);
                }
            }
        );
    }

    @Override
    public Talk talk(final int number) {
        return this.lru.cache(this.origin.talk(number));
    }

    @Override
    public void ask(final String text) {
        this.origin.ask(text);
    }

    @Override
    public Roll<Talk> history() {
        return this.hst.getOrSet(
            new Atomic.Source<Roll<Talk>>() {
                @Override
                public Roll<Talk> read() {
                    return new CdHistory(
                        CdHub.this.origin.history(), CdHub.this.lru
                    );
                }
            }
        );
    }

}
