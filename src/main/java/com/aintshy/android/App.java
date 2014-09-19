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
     * Hub to use.
     */
    private final transient Hub hub;

    /**
     * Map of objects.
     */
    private final transient ConcurrentMap<Class<?>, Object> objects =
        new ConcurrentHashMap<Class<?>, Object>(0);

    /**
     * Ctor.
     */
    public App() {
        super();
        final String token = "4CIB1GNA-UJOMU5DG-7KO2KJQL-0LFKS0HU-7C0GSK3F-99A18IJF-003L0L8V-0K4G2TOA-A1F3GEH7-8G9JOKIM-2174U0PU-4T30461G-E0Q4O91J-3CDLQ7OK-50======";
        this.hub = new CdHub(
            new RtEntrance().hub(token)
        );
    }

    @Override
    public <T> T fetch(final Class<T> type, final Bag.Source<T> src) {
        final T object;
        if (type.equals(Hub.class)) {
            object = type.cast(this.hub);
        } else {
            synchronized (this.objects) {
                if (!this.objects.containsKey(type)) {
                    this.objects.put(type, src.create());
                }
                object = type.cast(this.objects.get(type));
            }
        }
        return object;
    }
}
