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

import com.aintshy.android.api.History;
import com.aintshy.android.api.Talk;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import java.util.Collection;
import java.util.Date;
import lombok.EqualsAndHashCode;

/**
 * Cached History.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@EqualsAndHashCode(of = "origin")
final class CdHistory implements History {

    /**
     * Original object.
     */
    private final transient History origin;

    /**
     * Talks.
     */
    private final transient Atomic<Collection<Talk>> cached =
        new Atomic<Collection<Talk>>();

    /**
     * LRU cache.
     */
    private final transient LruTalks lru;

    /**
     * Ctor.
     * @param orgn Original
     * @param cache Cache
     */
    CdHistory(final History orgn, final LruTalks cache) {
        this.origin = orgn;
        this.lru = cache;
    }

    @Override
    public Collection<Talk> talks() {
        return Collections2.transform(
            this.cached.getOrSet(
                new Atomic.Source<Collection<Talk>>() {
                    @Override
                    public Collection<Talk> read() {
                        return CdHistory.this.origin.talks();
                    }
                }
            ),
            new Function<Talk, Talk>() {
                @Override
                public Talk apply(final Talk talk) {
                    return CdHistory.this.lru.cache(talk);
                }
            }
        );
    }

    @Override
    public History since(final Date date) {
        throw new UnsupportedOperationException("#since()");
    }

}
