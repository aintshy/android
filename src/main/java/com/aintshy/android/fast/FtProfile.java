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
package com.aintshy.android.fast;

import android.graphics.Bitmap;
import com.aintshy.android.api.Profile;

/**
 * Fast profile.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
final class FtProfile implements Profile {

    /**
     * Original profile.
     */
    private final transient Profile origin;

    /**
     * Ctor.
     * @param prf Original
     */
    FtProfile(final Profile prf) {
        this.origin = prf;
    }

    @Override
    public boolean confirmed() {
        throw new UnsupportedOperationException("#confirmed()");
    }

    @Override
    public void confirm(final String code) {
        throw new UnsupportedOperationException("#confirm()");
    }

    @Override
    public boolean complete() {
        throw new UnsupportedOperationException("#complete()");
    }

    @Override
    public void update(final String name, final int age, final char sex, final String lang) {
        throw new UnsupportedOperationException("#update()");
    }

    @Override
    public void upload(final byte[] photo) {
        throw new UnsupportedOperationException("#upload()");
    }

    @Override
    public String name() {
        throw new UnsupportedOperationException("#name()");
    }

    @Override
    public int age() {
        throw new UnsupportedOperationException("#age()");
    }

    @Override
    public char sex() {
        throw new UnsupportedOperationException("#sex()");
    }

    @Override
    public Bitmap photo() {
        throw new UnsupportedOperationException("#photo()");
    }
}
