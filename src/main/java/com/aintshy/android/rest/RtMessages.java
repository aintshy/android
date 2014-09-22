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
import com.aintshy.android.api.Message;
import com.aintshy.android.api.Roll;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.jcabi.http.Request;
import com.jcabi.http.response.RestResponse;
import com.jcabi.http.response.XmlResponse;
import com.jcabi.xml.XML;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import lombok.EqualsAndHashCode;

/**
 * RESTful messages in a talk.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@EqualsAndHashCode(of = "request")
final class RtMessages implements Roll<Message> {

    /**
     * HTTP request to the server.
     */
    private final transient Request request;

    /**
     * Ctor.
     * @param req Request to the front page
     */
    RtMessages(final Request req) {
        this.request = req;
    }

    @Override
    public Collection<Message> fetch(final int first, final int last) {
        try {
            final XML page = this.request.fetch()
                .as(RestResponse.class)
                .assertStatus(HttpURLConnection.HTTP_OK)
                .as(XmlResponse.class)
                .assertXPath("/page/human/urn")
                .xml()
                .nodes("/page").get(0);
            final String asking = page.xpath("role/asking/text()").get(0);
            final Collection<XML> nodes = page.nodes("messages/message");
            final List<Message> msgs = new ArrayList<Message>(nodes.size() + 1);
            msgs.addAll(
                Collections2.transform(
                    nodes,
                    new Function<XML, Message>() {
                        @Override
                        public Message apply(final XML xml) {
                            return new Message.Simple(
                                !asking.equals(
                                    xml.xpath("asking/text()").get(0)
                                ),
                                new Date(),
                                xml.xpath("text/text()").get(0)
                            );
                        }
                    }
                )
            );
            msgs.add(
                new Message.Simple(
                    "false".equals(asking),
                    new Date(),
                    page.xpath("talk/question/text()").get(0)
                )
            );
            Log.i(
                RtTalk.class.getName(),
                String.format("%d messages loaded", msgs.size())
            );
            return msgs;
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
