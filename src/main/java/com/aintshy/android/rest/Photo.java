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
package com.aintshy.android.rest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.jcabi.http.request.JdkRequest;
import com.jcabi.http.response.RestResponse;
import com.jcabi.http.wire.AutoRedirectingWire;
import java.io.IOException;
import java.net.HttpURLConnection;
import lombok.EqualsAndHashCode;

/**
 * RESTful photo.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@EqualsAndHashCode(of = "uri")
final class Photo {

    /**
     * URI.
     */
    private final transient String uri;

    /**
     * Ctor.
     * @param path Web path of the photo
     */
    Photo(final String path) {
        this.uri = path;
    }

    /**
     * Get it as a bitmap.
     * @return Bitmap
     * @throws IOException If fails
     */
    public Bitmap bitmap() throws IOException {
        final byte[] photo = new JdkRequest(this.uri)
            .through(AutoRedirectingWire.class)
            .fetch()
            .as(RestResponse.class)
            .assertStatus(HttpURLConnection.HTTP_OK)
            .binary();
        Log.i(
            RtTalk.class.getName(),
            String.format(
                "photo loaded from %s: %d bytes",
                this.uri, photo.length
            )
        );
        return BitmapFactory.decodeByteArray(photo, 0, photo.length);
    }
}
