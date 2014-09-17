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

import android.util.Log;
import com.aintshy.android.api.History;
import com.aintshy.android.api.Hub;
import com.aintshy.android.api.Profile;
import com.aintshy.android.api.Talk;
import com.jcabi.http.Request;
import com.jcabi.http.response.RestResponse;
import com.jcabi.http.response.XmlResponse;
import com.jcabi.http.wire.AutoRedirectingWire;
import com.jcabi.xml.XML;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import lombok.EqualsAndHashCode;

/**
 * RESTful Hub.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@EqualsAndHashCode(of = "request")
final class RtHub implements Hub {

    /**
     * HTTP request to the server.
     */
    private final transient Request request;

    /**
     * Ctor.
     * @param req Request to the front page
     */
    RtHub(final Request req) {
        this.request = req;
    }

    @Override
    public Profile profile() {
        return new RtProfile(this.request);
    }

    @Override
    public Iterable<Talk> next() {
        final XmlResponse response;
        try {
            response = this.request
                .through(AutoRedirectingWire.class)
                .fetch()
                .as(RestResponse.class)
                .assertStatus(HttpURLConnection.HTTP_OK)
                .as(XmlResponse.class)
                .assertXPath("/page/human/urn");
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
        final Collection<Talk> talks = new ArrayList<Talk>(1);
        final XML xml = response.xml();
        if (xml.nodes("/page/talk").isEmpty()) {
            Log.i(
                this.getClass().getName(),
                String.format(
                    "home page of %s [%s], no talks",
                    xml.xpath("/page/human/name/text()").get(0),
                    xml.xpath("/page/human/urn/text()").get(0)
                )
            );
            talks.add(
                new RtTalk(
                    response.back().uri()
                        .set(URI.create("http://i.aintshy.com/9")).back(),
                    9
                )
            );
        } else {
            talks.add(
                new RtTalk(
                    response.rel("/page/links/link[@rel='self']/@href"),
                    Integer.parseInt(
                        xml.xpath("/page/talk/number/text()").get(0)
                    )
                )
            );
            Log.i(
                this.getClass().getName(),
                String.format(
                    "home page with %s",
                    xml.xpath("/page/talk/number/text()").get(0)
                )
            );
        }
        return talks;
    }

    @Override
    public Talk talk(final int number) {
        return new RtTalk(
            this.request.uri()
                .path(String.format("/%d", number))
                .back(),
            number
        );
    }

    @Override
    public void ask(final String text) {
        try {
            this.request.uri().path("/empty").back()
                .fetch()
                .as(XmlResponse.class)
                .assertXPath("/page/human/name")
                .rel("/page/links/link[@rel='ask']/@href")
                .method(Request.POST)
                .body().formParam("text", text).back()
                .fetch()
                .as(RestResponse.class)
                .assertStatus(HttpURLConnection.HTTP_SEE_OTHER);
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public History history() {
        return new RtHistory(this.request);
    }

}
