/*
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2018 Vinicius Tona
 * Copyright (c) 2018 Antonio Morales
 * 
 * This file is part of gstreamer-java.
 *
 * This code is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License version 3 only, as published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License version 3 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License version 3 along with
 * this work. If not, see <http://www.gnu.org/licenses/>.
 */
package org.freedesktop.gstreamer;

import static org.freedesktop.gstreamer.lowlevel.GstPromiseAPI.GSTPROMISE_API;

import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.glib.Natives;

/**
 * A miniobject for future/promise-like functionality
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstPromise.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstPromise.html</a>
 * <p>
 */
@Gst.Since(minor = 14)
public class Promise extends MiniObject {

    public static final String GTYPE_NAME = "GstPromise";

    /**
     * Creates a new instance of Promise. This constructor is used internally.
     *
     * @param init internal initialization data.
     */
    Promise(final Initializer init) {
        super(init);
    }

    /**
     * Creates a new instance of promise
     */
    public Promise() {
        this(Natives.initializer(GSTPROMISE_API.ptr_gst_promise_new()));
        Gst.checkVersion(1, 14); // @TODO ideally this check would be before native call!
    }

    /**
     * Creates a new instance of promise with a callback attached.
     *
     * @param listener Listener to be called whenever the state of a
     * {@link Promise} is changed
     */
    public Promise(final PROMISE_CHANGE listener) {
        this(Natives.initializer(GSTPROMISE_API.ptr_gst_promise_new_with_change_func(new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(Promise promise, Pointer userData) {
                listener.onChange(promise);
            }
        }), false, false));
    }

    /**
     * Wait for the promise to move out of the PENDING {@link PromiseResult}
     * state. If the promise is not in PENDING then it will immediately return.
     *
     * @return the {@link PromiseResult} of the promise.
     */
    public PromiseResult waitResult() {
        return GSTPROMISE_API.gst_promise_wait(this);
    }

    /**
     * Set a reply on the promise.
     *
     * Will wake up any waiters on the promise with the REPLIED
     * {@link PromiseResult} state. If the promise has already been interrupted
     * than the replied will not be visible to any waiters
     *
     * @param structure the {@link Structure} to reply the promise with
     */
    public void reply(final Structure structure) {
        GSTPROMISE_API.gst_promise_reply(this, structure);
    }

    /**
     * Interrupt waiting for the result of the promise.
     *
     * Any waiters on the promise will receive the INTERRUPTED
     * {@link PromiseResult} state.
     */
    public void interrupt() {
        GSTPROMISE_API.gst_promise_interrupt(this);
    }

    /**
     * Expire a promise.
     *
     * Any waiters on the promise will received the EXPIRED
     * {@link PromiseResult} state.
     */
    public void expire() {
        GSTPROMISE_API.gst_promise_expire(this);
    }

    /**
     * Retrieve the reply set on the promise.
     *
     * The state of the promise must be in the REPLIED {@link PromiseResult}
     * state. The return structure is owned by the promise and thus cannot be
     * modified.
     *
     * @return the {@link Structure} set on the promise reply.
     */
    public Structure getReply() {
        return Structure.objectFor(GSTPROMISE_API.ptr_gst_promise_get_reply(this), false, false);
    }

    /**
     * Called whenever the state of the promise is changed from PENDING to any
     * other {@link PromiseResult}
     */
    public static interface PROMISE_CHANGE {

        /**
         * Called whenever the state of the promise is changed from PENDING to
         * any other {@link PromiseResult}
         *
         * @param promise the original promise that had the callback attached to
         */
        public void onChange(Promise promise);
    }
}
