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
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.aintshy.android.App;
import com.aintshy.android.R;
import com.aintshy.android.api.NoAuthException;
import com.aintshy.android.ui.EntranceActivity;

/**
 * Login activity.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
public final class LoginActivity extends Activity {

    @Override
    public void onCreate(final Bundle state) {
        super.onCreate(state);
        this.setContentView(R.layout.login_main);
        final EditText email = EditText.class.cast(
            this.findViewById(R.id.email)
        );
        final EditText password = EditText.class.cast(
            this.findViewById(R.id.password)
        );
        final Button button = Button.class.cast(
            this.findViewById(R.id.login_button)
        );
        button.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    final InputMethodManager imm =
                        InputMethodManager.class.cast(
                            LoginActivity.this.getSystemService(
                                Context.INPUT_METHOD_SERVICE
                            )
                        );
                    imm.hideSoftInputFromWindow(
                        password.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN
                    );
                    LoginActivity.this.authenticate(
                        email.getText().toString(),
                        password.getText().toString()
                    );
                }
            }
        );
    }

    /**
     * Authenticate.
     * @param email Email
     * @param password Password
     */
    private void authenticate(final String email, final String password) {
        Toast.makeText(this, "let's try...", Toast.LENGTH_SHORT).show();
        // @checkstyle AnonInnerLengthCheck (50 lines)
        new AsyncTask<Void, Integer, String>() {
            @Override
            public String doInBackground(final Void... none) {
                String error = "";
                try {
                    App.class.cast(
                        LoginActivity.this.getApplication()
                    ).authenticate(email, password);
                } catch (final NoAuthException ex) {
                    error = ex.getLocalizedMessage();
                }
                return error;
            }
            @Override
            public void onPostExecute(final String error) {
                if (error.isEmpty()) {
                    LoginActivity.this.startActivity(
                        new Intent(LoginActivity.this, EntranceActivity.class)
                    );
                } else {
                    Toast.makeText(
                        LoginActivity.this, error, Toast.LENGTH_SHORT
                    ).show();
                }
            }
        } .execute();
    }

}
