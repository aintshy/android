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
package com.aintshy.android;

import android.app.Application;
import android.content.SharedPreferences;
import com.aintshy.android.api.Hub;
import com.aintshy.android.cached.CdHub;
import com.aintshy.android.rest.RtEntrance;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Application.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
public final class App extends Application implements Bag {

    /**
     * Map of objects.
     */
    private final transient ConcurrentMap<Class<?>, Object> objects =
        new ConcurrentHashMap<Class<?>, Object>(0);

    /**
     * Authenticate.
     * @param email Email
     * @param password Password
     */
    public void authenticate(final String email, final String password) {
        this.objects.put(
            Hub.class,
            new CdHub(new RtEntrance().auth(email, password))
        );
        final SharedPreferences.Editor editor =
            this.getSharedPreferences("aintshy", 0).edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }

    @Override
    public <T> T fetch(final Class<T> type, final Bag.Source<T> src) {
        final T object;
        synchronized (this.objects) {
            if (!this.objects.containsKey(type)) {
                this.objects.put(type, src.create());
            }
            object = type.cast(this.objects.get(type));
        }
        return object;
    }

}
