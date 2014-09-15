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
import com.aintshy.android.api.Human;
import com.jcabi.http.Request;
import com.jcabi.http.response.RestResponse;
import com.jcabi.http.response.XmlResponse;
import com.jcabi.http.wire.AutoRedirectingWire;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * RESTful role.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
final class RtRole implements Human {

    /**
     * HTTP request.
     */
    private final transient Request request;

    /**
     * Ctor.
     * @param req Request
     */
    RtRole(final Request req) {
        this.request = req;
    }

    @Override
    public String name() {
        try {
            return this.request.fetch()
                .as(RestResponse.class)
                .assertStatus(HttpURLConnection.HTTP_OK)
                .as(XmlResponse.class)
                .assertXPath("/page/role/name")
                .xml()
                .xpath("/page/role/name/text()")
                .get(0);
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public int age() {
        try {
            return Integer.parseInt(
                this.request.fetch()
                    .as(RestResponse.class)
                    .assertStatus(HttpURLConnection.HTTP_OK)
                    .as(XmlResponse.class)
                    .assertXPath("/page/role/age")
                    .xml()
                    .xpath("/page/role/age/text()")
                    .get(0)
            );
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public char sex() {
        try {
            return this.request.fetch()
                .as(RestResponse.class)
                .assertStatus(HttpURLConnection.HTTP_OK)
                .as(XmlResponse.class)
                .assertXPath("/page/role/sex")
                .xml()
                .xpath("/page/role/sex/text()")
                .get(0).charAt(0);
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public Bitmap photo() {
        final byte[] photo;
        try {
            photo = this.request.fetch()
                .as(XmlResponse.class)
                .rel("/page/role/links/link[@rel='photo']/@href")
                .through(AutoRedirectingWire.class)
                .fetch()
                .as(RestResponse.class)
                .assertStatus(HttpURLConnection.HTTP_OK)
                .binary();
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
        Log.i(
            RtTalk.class.getName(),
            String.format(
                "photo loaded for %s: %d bytes",
                this.name(), photo.length
            )
        );
        return BitmapFactory.decodeByteArray(photo, 0, photo.length);
    }
}
