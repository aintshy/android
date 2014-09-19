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
import com.aintshy.android.api.Roll;
import com.aintshy.android.api.Talk;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.jcabi.http.Request;
import com.jcabi.http.response.RestResponse;
import com.jcabi.http.response.XmlResponse;
import com.jcabi.xml.XML;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import lombok.EqualsAndHashCode;

/**
 * RESTful History.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@EqualsAndHashCode(of = "request")
final class RtHistory implements Roll<Talk> {

    /**
     * HTTP request to the server.
     */
    private final transient Request request;

    /**
     * Ctor.
     * @param req Request to the front page
     */
    RtHistory(final Request req) {
        this.request = req.uri().path("/history").back();
    }

    @Override
    public Collection<Talk> fetch(final int first, final int last) {
        final XmlResponse response;
        try {
            response = this.request
                .fetch()
                .as(RestResponse.class)
                .assertStatus(HttpURLConnection.HTTP_OK)
                .as(XmlResponse.class)
                .assertXPath("/page/human/urn");
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
        final List<Talk> list = Lists.transform(
            response.xml().nodes("/page/history/story"),
            new Function<XML, Talk>() {
                @Override
                public Talk apply(final XML xml) {
                    return new RtTalk(
                        RtHistory.this.request.uri().set(
                            URI.create(
                                xml.xpath(
                                    "links/link[@rel='open']/@href"
                                ).get(0)
                            )
                        ).back(),
                        Integer.parseInt(xml.xpath("number/text()").get(0))
                    );
                }
            }
        );
        Log.i(
            this.getClass().getName(),
            String.format("found %d talks in history", list.size())
        );
        return list;
    }
}
