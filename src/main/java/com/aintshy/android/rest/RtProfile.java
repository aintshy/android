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

import com.aintshy.android.api.CodeConfirmException;
import com.aintshy.android.api.Human;
import com.aintshy.android.api.Profile;
import com.aintshy.android.api.ProfileUpdateException;
import com.google.common.collect.ImmutableMap;
import com.google.common.net.HttpHeaders;
import com.jcabi.http.Request;
import com.jcabi.http.Response;
import com.jcabi.http.response.RestResponse;
import com.jcabi.http.response.XmlResponse;
import com.jcabi.log.Logger;
import com.jcabi.xml.XML;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Locale;
import javax.ws.rs.core.MediaType;
import lombok.EqualsAndHashCode;

/**
 * RESTful Profile.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@EqualsAndHashCode(of = "request")
final class RtProfile implements Profile {

    /**
     * Language two-letter codes.
     */
    private static final ImmutableMap<Locale, String> CODES =
        new ImmutableMap.Builder<Locale, String>()
            .put(Locale.ENGLISH, "en")
            .put(new Locale("Arabic"), "ar")
            .put(Locale.CHINESE, "zh")
            .put(new Locale("Hindi"), "hi")
            .put(new Locale("Spanish"), "sp")
            .put(new Locale("Russian"), "ru")
            .build();

    /**
     * HTTP request to the server.
     */
    private final transient Request request;

    /**
     * Ctor.
     * @param req Request to the front page
     */
    RtProfile(final Request req) {
        this.request = req;
    }

    @Override
    public boolean confirmed() {
        try {
            return this.empty()
                .fetch()
                .as(RestResponse.class)
                .assertStatus(HttpURLConnection.HTTP_OK)
                .as(XmlResponse.class)
                .assertXPath("/page/human/urn")
                .xml()
                .nodes("/page/human[@confirmed='false']")
                .isEmpty();
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public void confirm(final String code) {
        try {
            final Response response = this.request
                .uri()
                .path("/setup/confirm")
                .back()
                .method(Request.POST)
                .body().formParam("code", code).back()
                .header(
                    HttpHeaders.CONTENT_TYPE,
                    MediaType.APPLICATION_FORM_URLENCODED
                )
                .fetch()
                .as(RestResponse.class)
                .assertStatus(HttpURLConnection.HTTP_SEE_OTHER);
            final XML page = this.empty()
                .fetch()
                .as(RestResponse.class)
                .assertStatus(HttpURLConnection.HTTP_OK)
                .as(XmlResponse.class)
                .xml();
            if (page.nodes("/page/human[@confirmed='true']").isEmpty()) {
                throw new CodeConfirmException(this.flash(response));
            }
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public void resend() {
        try {
            this.request
                .uri().path("/setup/not-confirmed").back()
                .fetch()
                .as(RestResponse.class)
                .assertStatus(HttpURLConnection.HTTP_OK)
                .as(XmlResponse.class)
                .rel("/page/links/link[@rel='resend']/@href")
                .method(Request.POST)
                .fetch()
                .as(RestResponse.class)
                .assertStatus(HttpURLConnection.HTTP_SEE_OTHER);
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    // @checkstyle ParameterNumberCheck (5 lines)
    @Override
    public void update(final String name, final int year,
        final char sex, final Locale lang) {
        Logger.info(this, "LANG: %s", lang);
        try {
            final Response response = this.request
                .uri()
                .path("/setup/details")
                .back()
                .method(Request.POST)
                .body()
                .formParam("name", name)
                .formParam("year", year)
                .formParam("sex", sex)
                .formParam("lang", RtProfile.CODES.get(lang))
                .back()
                .header(
                    HttpHeaders.CONTENT_TYPE,
                    MediaType.APPLICATION_FORM_URLENCODED
                )
                .fetch()
                .as(RestResponse.class)
                .assertStatus(HttpURLConnection.HTTP_SEE_OTHER);
            final XML page = this.empty()
                .fetch()
                .as(RestResponse.class)
                .assertStatus(HttpURLConnection.HTTP_OK)
                .as(XmlResponse.class)
                .xml();
            if (page.nodes("/page/human[age!='0']").isEmpty()) {
                throw new ProfileUpdateException(this.flash(response));
            }
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public void upload(final byte[] photo) {
        try {
            this.request
                .uri()
                .path("/profile/upload")
                .back()
                .method(Request.POST)
                .body()
                .formParam("photo", photo)
                .back()
                .header(
                    HttpHeaders.CONTENT_TYPE,
                    MediaType.MULTIPART_FORM_DATA
                )
                .fetch()
                .as(RestResponse.class)
                .assertStatus(HttpURLConnection.HTTP_SEE_OTHER);
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public Human myself() {
        try {
            final XML xml = this.empty()
                .fetch()
                .as(RestResponse.class)
                .assertStatus(HttpURLConnection.HTTP_OK)
                .as(XmlResponse.class)
                .assertXPath("/page/human/age")
                .xml()
                .nodes("/page/human").get(0);
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

    /**
     * Empty entrance.
     * @return Request
     */
    private Request empty() {
        return this.request.uri().path("/empty").back();
    }

    /**
     * Get flash.
     * @param response Response
     * @return Flash message
     */
    private String flash(final Response response) {
        return response.headers().get("X-Rexsl-Flash").get(0);
    }

}
