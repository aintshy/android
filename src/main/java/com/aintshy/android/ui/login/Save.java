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
package com.aintshy.android.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;
import com.aintshy.android.ui.EntranceActivity;

/**
 * Save action.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
final class Save {

    /**
     * Home activity.
     */
    private final transient Activity home;

    /**
     * Action.
     */
    private final transient Save.Action action;

    /**
     * Ctor.
     * @param activity Home
     * @param act Action
     */
    Save(final Activity activity, final Save.Action act) {
        this.home = activity;
        this.action = act;
    }

    /**
     * On click of this button.
     * @param button The button
     */
    public void onClick(final Button button) {
        button.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    final InputMethodManager imm =
                        InputMethodManager.class.cast(
                            Save.this.home.getSystemService(
                                Context.INPUT_METHOD_SERVICE
                            )
                        );
                    imm.hideSoftInputFromWindow(
                        button.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN
                    );
                    Save.this.update();
                }
            }
        );
    }

    /**
     * Update it.
     */
    private void update() {
        Toast.makeText(this.home, "let's try...", Toast.LENGTH_SHORT).show();
        // @checkstyle AnonInnerLengthCheck (50 lines)
        new AsyncTask<Void, Integer, String>() {
            @Override
            public String doInBackground(final Void... none) {
                String error = "";
                try {
                    Save.this.action.exec();
                } catch (final Save.Failure ex) {
                    error = ex.getLocalizedMessage();
                }
                return error;
            }
            @Override
            public void onPostExecute(final String error) {
                if (error.isEmpty()) {
                    Save.this.home.startActivity(
                        new Intent(Save.this.home, EntranceActivity.class)
                    );
                } else {
                    Toast.makeText(
                        Save.this.home, error, Toast.LENGTH_SHORT
                    ).show();
                }
            }
        } .execute();
    }

    /**
     * Action.
     */
    public interface Action {
        /**
         * Execute it.
         * @throws Save.Failure If fails
         */
        void exec() throws Save.Failure;
    }

    /**
     * Action.
     */
    public static final class Failure extends Exception {
        /**
         * Serialization marker.
         */
        private static final long serialVersionUID = 2586077576750735704L;
        /**
         * Ctor.
         * @param cause Cause
         */
        public Failure(final String cause) {
            super(cause);
        }
        /**
         * Ctor.
         * @param cause Cause
         */
        public Failure(final Exception cause) {
            super(cause);
        }
    }

}
