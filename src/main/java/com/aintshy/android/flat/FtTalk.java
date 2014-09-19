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
package com.aintshy.android.flat;

import com.aintshy.android.api.Human;
import com.aintshy.android.api.Message;
import com.aintshy.android.api.Roll;
import com.aintshy.android.api.Talk;
import java.util.Collection;
import java.util.Collections;
import lombok.EqualsAndHashCode;

/**
 * Flat Talk.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@EqualsAndHashCode(of = { "num", "rle", "last" })
public final class FtTalk implements Talk {

    /**
     * Its number.
     */
    private final transient int num;

    /**
     * Role.
     */
    private final transient Human rle;

    /**
     * Last message.
     */
    private final transient Message last;

    /**
     * Ctor.
     * @param talk Original talk
     */
    public FtTalk(final Talk talk) {
        this.num = talk.number();
        final Human role = talk.role();
        this.rle = new Human.Simple(
            role.name(), role.age(), role.sex(), role.photo()
        );
        this.last = talk.messages().fetch(0, 0).iterator().next();
    }

    @Override
    public int number() {
        return this.num;
    }

    @Override
    public Human role() {
        return this.rle;
    }

    @Override
    public Roll<Message> messages() {
        return new Roll<Message>() {
            @Override
            public Collection<Message> fetch(final int start, final int end) {
                return Collections.singleton(FtTalk.this.last);
            }
        };
    }

    @Override
    public void post(final String text) {
        throw new UnsupportedOperationException("#post()");
    }

}
