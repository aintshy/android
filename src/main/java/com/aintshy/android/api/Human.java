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

import android.graphics.Bitmap;
import lombok.EqualsAndHashCode;

/**
 * Human.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
public interface Human {

    /**
     * Get name.
     * @return Name
     */
    String name();

    /**
     * Get age.
     * @return Age
     */
    int age();

    /**
     * Get sex.
     * @return Sex
     */
    char sex();

    /**
     * Get photo, as PNG image.
     * @return Photo
     */
    Bitmap photo();

    /**
     * Simple.
     */
    @EqualsAndHashCode(of = "label")
    final class Simple implements Human {
        /**
         * Name.
         */
        private final transient String label;
        /**
         * Year of birth.
         */
        private final transient int years;
        /**
         * Sex.
         */
        private final transient char gender;
        /**
         * Photo.
         */
        private final transient Bitmap png;
        /**
         * Ctor.
         * @param name Name
         * @param age Age
         * @param sex Sex
         * @param photo Photo
         * @checkstyle ParameterNumberCheck (5 lines)
         */
        public Simple(final String name, final int age, final char sex,
            final Bitmap photo) {
            this.label = name;
            this.years = age;
            this.gender = sex;
            this.png = photo;
        }
        @Override
        public String name() {
            return this.label;
        }
        @Override
        public int age() {
            return this.years;
        }
        @Override
        public char sex() {
            return this.gender;
        }
        @Override
        public Bitmap photo() {
            return this.png;
        }
    }

}
