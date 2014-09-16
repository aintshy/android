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

import com.aintshy.android.api.Human;
import com.aintshy.android.api.Message;
import com.aintshy.android.api.Talk;
import java.util.Collection;
import java.util.Date;
import lombok.EqualsAndHashCode;

/**
 * Cached Talk.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@EqualsAndHashCode(of = "origin")
final class CdTalk implements Talk {

    /**
     * Original object.
     */
    private final transient Talk origin;

    /**
     * Role.
     */
    private final transient Atomic<Human> human = new Atomic<Human>();

    /**
     * Messages.
     */
    private final transient Atomic<Collection<Message>> msgs =
        new Atomic<Collection<Message>>();

    /**
     * Ctor.
     * @param orgn Original
     */
    CdTalk(final Talk orgn) {
        this.origin = orgn;
    }

    @Override
    public Human role() {
        return this.human.getOrSet(
            new Atomic.Source<Human>() {
                @Override
                public Human read() {
                    return CdTalk.this.origin.role();
                }
            }
        );
    }

    @Override
    public Collection<Message> messages() {
        return this.msgs.getOrSet(
            new Atomic.Source<Collection<Message>>() {
                @Override
                public Collection<Message> read() {
                    return CdTalk.this.origin.messages();
                }
            }
        );
    }

    @Override
    public void post(final String text) {
        this.msgs.flush();
        this.origin.post(text);
    }

    @Override
    public Talk since(final Date date) {
        return this.origin.since(date);
    }
}
