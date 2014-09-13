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

import com.aintshy.android.api.Human;
import com.aintshy.android.api.Message;
import com.aintshy.android.api.Talk;

/**
 * Fat talk.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
public final class FtTalk implements Talk {

    /**
     * Original talk.
     */
    private final transient Talk origin;

    /**
     * Original talker.
     */
    private final transient Human human;

    /**
     * Ctor.
     * @param tlk Original
     */
    FtTalk(final Talk tlk) {
        this.origin = tlk;
        this.human = tlk.talker();
    }

    @Override
    public Human talker() {
        return new Human.Simple(
            this.human.name(), this.human.age(), this.human.sex(),
            this.human.photo()
        );
    }

    @Override
    public Iterable<Message> messages() {
        return this.origin.messages();
    }

    @Override
    public void post(final String text) {
        this.origin.post(text);
    }
}
