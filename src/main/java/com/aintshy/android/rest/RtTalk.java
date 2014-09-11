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
import com.aintshy.android.api.Talk;
import com.jcabi.http.Request;
import com.jcabi.http.response.XmlResponse;
import java.util.Arrays;
import java.util.Date;

/**
 * RESTful Talk.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
final class RtTalk implements Talk {

    /**
     * HTTP request to the server.
     */
    private final transient Request request;

    /**
     * Page retrieved.
     */
    private final transient XmlResponse page;

    /**
     * Ctor.
     * @param req Request to the front page
     */
    RtTalk(final Request req) {
        this.request = req;
        this.page = null;
//        this.page = req.fetch()
//            .as(RestResponse.class)
//            .assertStatus(HttpURLConnection.HTTP_OK)
//            .as(XmlResponse.class)
//            .assertXPath("/page/identity");
    }

    @Override
    public Human talker() {
        return new Human.Simple("Jeff", 33, 'M', new byte[0]);
//        final XML xml = this.page.xml().nodes("/page").get(0);
//        return new Human.Simple(
//            xml.xpath("role/name/text()").get(0),
//            Integer.parseInt(xml.xpath("role/age/text()").get(0)),
//            xml.xpath("role/sex/text()").get(0).charAt(0),
//            new byte[0]
//        );
    }

    @Override
    public Iterable<Message> messages() {
        return Arrays.<Message>asList(
            new Message.Simple(true, new Date(), "how are you?"),
            new Message.Simple(true, new Date(), "how are you?"),
            new Message.Simple(true, new Date(), "how are you?"),
            new Message.Simple(true, new Date(), "how are you?"),
            new Message.Simple(true, new Date(), "how are you?"),
            new Message.Simple(true, new Date(), "how are you?"),
            new Message.Simple(true, new Date(), "how are you?"),
            new Message.Simple(true, new Date(), "how are you?"),
            new Message.Simple(true, new Date(), "how are you?"),
            new Message.Simple(true, new Date(), "how are you?"),
            new Message.Simple(true, new Date(), "how are you?"),
            new Message.Simple(true, new Date(), "how are you?"),
            new Message.Simple(true, new Date(), "how are you?"),
            new Message.Simple(true, new Date(), "how are you?"),
            new Message.Simple(true, new Date(), "how are you?"),
            new Message.Simple(true, new Date(), "how are you?"),
            new Message.Simple(true, new Date(), "how are you?"),
            new Message.Simple(true, new Date(), "how are you?"),
            new Message.Simple(true, new Date(), "how are you?"),
            new Message.Simple(true, new Date(), "how are you?"),
            new Message.Simple(true, new Date(), "how are you?"),
            new Message.Simple(true, new Date(), "how are you?"),
            new Message.Simple(false, new Date(), "I'm fine!")
        );
    }

    @Override
    public void post(final String text) {
        throw new UnsupportedOperationException("#post()");
    }
}
