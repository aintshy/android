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

import java.util.concurrent.atomic.AtomicReference;

/**
 * Atomic cache.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
interface Atomic<T> {

    /**
     * Get or set.
     * @param src Source
     * @return Value
     */
    T getOrSet(Atomic.Source<T> src);

    /**
     * Flush it.
     */
    void flush();

    /**
     * Source.
     */
    interface Source<T> {
        /**
         * Get it.
         * @return Value
         */
        T read();
    }

    /**
     * Default.
     * @param <T> Type of data
     */
    final class Default<T> implements Atomic<T> {
        /**
         * Value.
         */
        private final transient AtomicReference<T> value =
            new AtomicReference<T>();
        @Override
        public T getOrSet(final Atomic.Source<T> src) {
            synchronized (this.value) {
                if (this.value.get() == null) {
                    this.value.set(src.read());
                }
                return this.value.get();
            }
        }
        @Override
        public void flush() {
            this.value.set(null);
        }
    }

}
