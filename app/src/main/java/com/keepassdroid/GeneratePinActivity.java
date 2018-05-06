/*
 * Copyright 2010 Tolga Onbay, Brian Pellin.
 *     
 * This file is part of KeePassDroid.
 *
 *  KeePassDroid is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  KeePassDroid is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with KeePassDroid.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.keepassdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.keepass.KeePass;
import com.android.keepass.R;
import com.patarapolw.diceware_utils.DicewarePassword;

public class GeneratePinActivity extends LockCloseActivity {
	DicewarePassword dicewarePassword;

	EditText digitCount;

	public static void Launch(Activity act) {
		Intent i = new Intent(act, GeneratePinActivity.class);

		act.startActivityForResult(i, 0);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generate_password_pin);
		setResult(KeePass.EXIT_NORMAL);

		dicewarePassword = new DicewarePassword(getApplicationContext());

		digitCount = (EditText) findViewById(R.id.pin_digit_count);

		Button genPassButton = (Button) findViewById(R.id.pin_generate_button);
        genPassButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				fillPassword();
			}
		});

        Button acceptButton = (Button) findViewById(R.id.pin_accept_button);
        acceptButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				EditText password = (EditText) findViewById(R.id.pin);
				EditText mnemonic = (EditText) findViewById(R.id.pin_mnemonic);
				
				Intent intent = new Intent();
				intent.putExtra("com.keepassdroid.password.generated_password", password.getText().toString());
				intent.putExtra("com.patarapolw.diceware_utils.generated_mnemonic", mnemonic.getText().toString());
				
				setResult(EntryEditActivity.RESULT_OK_PASSWORD_GENERATOR, intent);
				
				finish();
			}
		});
        
        Button cancelButton = (Button) findViewById(R.id.pin_cancel_button);
        cancelButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				
				finish();
			}
		});
        
        // Pre-populate a password to possibly save the user a few clicks
        fillPassword();
	}
	
	private void fillPassword() {
		EditText txtPassword = (EditText) findViewById(R.id.pin);
		EditText txtMnemonic = (EditText) findViewById(R.id.pin_mnemonic);
		generatePin();
		txtPassword.setText(dicewarePassword.getPin());
		txtMnemonic.setText(TextUtils.join(" ", dicewarePassword.getKeywordList()));
	}
	
    public void generatePin() {
		EditText digitCount = (EditText) findViewById(R.id.pin_digit_count);
    	dicewarePassword.generatePin(Integer.parseInt(digitCount.getText().toString()));
    }
}
