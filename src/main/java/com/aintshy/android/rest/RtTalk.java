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

import com.aintshy.android.api.Human;
import com.aintshy.android.api.Message;
import com.aintshy.android.api.Roll;
import com.aintshy.android.api.Talk;
import com.google.common.net.HttpHeaders;
import com.jcabi.http.Request;
import com.jcabi.http.response.RestResponse;
import com.jcabi.http.response.XmlResponse;
import com.jcabi.xml.XML;
import java.io.IOException;
import java.net.HttpURLConnection;
import javax.ws.rs.core.MediaType;
import lombok.EqualsAndHashCode;

/**
 * RESTful Talk.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@EqualsAndHashCode(of = "request")
final class RtTalk implements Talk {

    /**
     * HTTP request to the server.
     */
    private final transient Request request;

    /**
     * Number of it.
     */
    private final transient int num;

    /**
     * Ctor.
     * @param req Request to the front page
     * @param number Number
     */
    RtTalk(final Request req, final int number) {
        this.request = req;
        this.num = number;
    }

    @Override
    public int number() {
        return this.num;
    }

    @Override
    public Human role() {
        try {
            final XML xml = this.request.fetch()
                .as(RestResponse.class)
                .assertStatus(HttpURLConnection.HTTP_OK)
                .as(XmlResponse.class)
                .assertXPath("/page/role/name")
                .xml()
                .nodes("/page/role").get(0);
            return new Human.Simple(
                xml.xpath("name/text()").get(0),
                Integer.parseInt(xml.xpath("age/text()").get(0)),
                xml.xpath("sex/text()").get(0).charAt(0),
                new Photo(
                    xml.xpath("links/link[@rel='photo']/@href").get(0)
                ).bitmap()
            );
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public Roll<Message> messages() {
        return new RtMessages(this.request);
    }

    @Override
    public void post(final String text) {
        try {
            this.request.fetch()
                .as(RestResponse.class)
                .assertStatus(HttpURLConnection.HTTP_OK)
                .as(XmlResponse.class)
                .assertXPath("/page/human/name")
                .rel("/page/links/link[@rel='post']/@href")
                .method(Request.POST)
                .body().formParam("text", text).back()
                .header(
                    HttpHeaders.CONTENT_TYPE,
                    MediaType.APPLICATION_FORM_URLENCODED
                )
                .fetch()
                .as(RestResponse.class)
                .assertStatus(HttpURLConnection.HTTP_SEE_OTHER);
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

}
