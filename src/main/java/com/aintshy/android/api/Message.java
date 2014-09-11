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
package com.aintshy.android.api;

import java.util.Date;

/**
 * Message.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
public interface Message {

    /**
     * Is it my message?
     * @return TRUE if it's mine
     */
    boolean mine();

    /**
     * Date.
     * @return Date of it
     */
    Date date();

    /**
     * Text.
     * @return Text of it
     */
    String text();

    /**
     * Simple.
     */
    final class Simple implements Message {
        private final transient boolean self;
        private final transient Date when;
        private final transient String txt;

        public Simple(final boolean mine, final Date date, final String text) {
            this.self = mine;
            this.when = date;
            this.txt = text;
        }

        @Override
        public boolean mine() {
            return this.self;
        }

        @Override
        public Date date() {
            return this.when;
        }

        @Override
        public String text() {
            return this.txt;
        }
    }

}
