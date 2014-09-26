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
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import com.aintshy.android.Entrance;
import com.aintshy.android.R;
import com.aintshy.android.api.CodeConfirmException;

/**
 * Confirm activity.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
public final class ConfirmActivity extends Activity {

    @Override
    public void onCreate(final Bundle state) {
        super.onCreate(state);
        this.setContentView(R.layout.login_confirm);
        final EditText code = EditText.class.cast(
            this.findViewById(R.id.code)
        );
        new Save(
            this,
            new Save.Action() {
                @Override
                public void exec() throws Save.Failure {
                    ConfirmActivity.this.confirm(
                        code.getText().toString()
                    );
                }
            }
        ).onClick(Button.class.cast(this.findViewById(R.id.confirm_button)));
        new Save(
            this,
            new Save.Action() {
                @Override
                public void exec() {
                    new Entrance(ConfirmActivity.this).hub().profile().resend();
                }
            }
        ).onClick(Button.class.cast(this.findViewById(R.id.resend_button)));
    }

    /**
     * Confirm it.
     * @param code Code
     * @throws Save.Failure If fails
     */
    private void confirm(final String code) throws Save.Failure {
        try {
            new Entrance(this).hub().profile().confirm(code);
        } catch (final CodeConfirmException ex) {
            throw new Save.Failure(ex);
        }
    }

}
