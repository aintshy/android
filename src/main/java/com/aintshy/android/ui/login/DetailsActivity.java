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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.aintshy.android.Entrance;
import com.aintshy.android.R;
import com.aintshy.android.api.ProfileUpdateException;
import java.util.Locale;

/**
 * Details activity.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
public final class DetailsActivity extends Activity {

    @Override
    public void onCreate(final Bundle state) {
        super.onCreate(state);
        this.setContentView(R.layout.login_details);
        final EditText name = EditText.class.cast(
            this.findViewById(R.id.name)
        );
        final EditText year = EditText.class.cast(
            this.findViewById(R.id.year)
        );
        final Spinner sex = this.sexSpinner(
            Spinner.class.cast(this.findViewById(R.id.sex))
        );
        final Spinner language = this.langSpinner(
            Spinner.class.cast(this.findViewById(R.id.language))
        );
        new Save(
            this,
            new Save.Action() {
                @Override
                public void exec() throws Save.Failure {
                    DetailsActivity.this.update(
                        name.getText().toString(),
                        year.getText().toString(),
                        sex.getSelectedItem().toString(),
                        language.getSelectedItem().toString()
                    );
                }
            }
        ).onClick(Button.class.cast(this.findViewById(R.id.register_button)));
    }

    /**
     * Configure sex spinner.
     * @param spinner To configure
     * @return Ready to use
     */
    private Spinner sexSpinner(final Spinner spinner) {
        final ArrayAdapter<CharSequence> adapter =
            ArrayAdapter.createFromResource(
                this,
                R.array.genders, android.R.layout.simple_spinner_item
            );
        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        );
        spinner.setAdapter(adapter);
        return spinner;
    }

    /**
     * Configure lang spinner.
     * @param spinner To configure
     * @return Ready to use
     */
    private Spinner langSpinner(final Spinner spinner) {
        final ArrayAdapter<CharSequence> adapter =
            ArrayAdapter.createFromResource(
                this,
                R.array.languages, android.R.layout.simple_spinner_item
            );
        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        );
        spinner.setAdapter(adapter);
        return spinner;
    }

    /**
     * Update it.
     * @param name Name
     * @param year Year
     * @param sex Sex
     * @param lang Language
     * @throws Save.Failure If fails
     * @checkstyle ParameterNumberCheck (5 lines)
     */
    @SuppressWarnings("PMD.UseObjectForClearerAPI")
    private void update(final String name, final String year,
        final String sex, final String lang) throws Save.Failure {
        if (!year.matches("19[0-9]{2}")) {
            throw new Save.Failure("year of birth, huh?");
        }
        try {
            new Entrance(this).hub().profile().update(
                name, Integer.parseInt(year),
                sex.toUpperCase(Locale.ENGLISH).charAt(0),
                new Locale(lang)
            );
        } catch (final ProfileUpdateException ex) {
            throw new Save.Failure(ex);
        }
    }

}
