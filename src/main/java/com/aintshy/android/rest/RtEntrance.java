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

import com.aintshy.android.api.Hub;
import com.aintshy.android.api.NoAuthException;
import com.google.common.net.HttpHeaders;
import com.jcabi.http.Request;
import com.jcabi.http.Response;
import com.jcabi.http.request.JdkRequest;
import com.jcabi.http.response.RestResponse;
import com.jcabi.http.wire.CachingWire;
import com.jcabi.http.wire.RetryWire;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Collection;
import javax.ws.rs.core.MediaType;

/**
 * RESTful authentication.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
public final class RtEntrance {

    /**
     * Authenticate with email and password, and get a token.
     * @param email Email
     * @param password Password
     * @return Hub
     */
    public Hub auth(final String email, final String password) {
        final String token = this.token(email, password);
        final Request req = new JdkRequest("http://i.aintshy.com/")
            .header(HttpHeaders.COOKIE, String.format("Rexsl-Auth=%s", token))
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML)
            .header(HttpHeaders.USER_AGENT, "Android app")
            .through(RetryWire.class)
            .through(CachingWire.class);
        try {
            final Response response = req.uri().path("/empty").back().fetch();
            if (response.status() != HttpURLConnection.HTTP_OK) {
                throw new NoAuthException("wrong token");
            }
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
        return new RtHub(req);
    }

    /**
     * Get token.
     * @param email Email
     * @param password Password
     * @return Token
     */
    private String token(final String email, final String password) {
        try {
            final Response page = new JdkRequest("http://i.aintshy.com/enter")
                .body().formParam("email", email)
                .formParam("password", password).back()
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML)
                .method(Request.POST)
                .fetch()
                .as(RestResponse.class)
                .assertStatus(HttpURLConnection.HTTP_SEE_OTHER);
            final Collection<String> token =
                page.headers().get("X-Aintshy-Token");
            if (token == null) {
                throw new NoAuthException(
                    page.headers().get("X-Rexsl-Flash").get(0)
                );
            }
            return token.iterator().next();
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

}
