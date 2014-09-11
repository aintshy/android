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
import com.aintshy.android.api.Profile;
import com.aintshy.android.api.Talk;
import com.jcabi.http.Request;
import java.io.IOException;
import java.util.Collections;

/**
 * RESTful Hub.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
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
        return Collections.<Talk>singleton(
            new RtTalk(this.request)
        );
    }

    @Override
    public void ask(final String text) {
        throw new UnsupportedOperationException("#ask()");
    }

    @Override
    public Iterable<Talk> history() {
        return Collections.<Talk>singleton(new RtTalk(this.request));
    }

}
