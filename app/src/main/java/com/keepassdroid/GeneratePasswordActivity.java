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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.keepass.KeePass;
import com.android.keepass.R;
import com.keepassdroid.password.PasswordGenerator;
import com.patarapolw.diceware_utils.GenerateDicewarePassword;

import org.json.JSONException;
import org.json.JSONObject;

public class GeneratePasswordActivity extends LockCloseActivity {
	GenerateDicewarePassword generateDicewarePassword;

	EditText digitCount;
	EditText punctuationCount;
	EditText lengthMin;
	EditText lengthMax;
	
	public static void Launch(Activity act) {
		Intent i = new Intent(act, GeneratePasswordActivity.class);
		
		act.startActivityForResult(i, 0);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generate_password);
		setResult(KeePass.EXIT_NORMAL);

		generateDicewarePassword = new GenerateDicewarePassword(getApplicationContext());
		digitCount = (EditText) findViewById(R.id.digit_count);
		punctuationCount = (EditText) findViewById(R.id.punctuation_count);
		lengthMin = (EditText) findViewById(R.id.length_from);
		lengthMax = (EditText) findViewById(R.id.length_to);
		
		Button genPassButton = (Button) findViewById(R.id.generate_password_button);
        genPassButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				fillPassword();
			}
		});
        
        Button acceptButton = (Button) findViewById(R.id.accept_button);
        acceptButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				EditText password = (EditText) findViewById(R.id.password);
				
				Intent intent = new Intent();
				intent.putExtra("com.keepassdroid.password.generated_password", password.getText().toString());
				
				setResult(EntryEditActivity.RESULT_OK_PASSWORD_GENERATOR, intent);
				
				finish();
			}
		});
        
        Button cancelButton = (Button) findViewById(R.id.cancel_button);
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
		EditText txtPassword = (EditText) findViewById(R.id.password);
		txtPassword.setText(generatePassword());
	}
	
    public String generatePassword() {
        JSONObject passwordOptions = new JSONObject();
        try {
            passwordOptions.put("digit_count", Integer.parseInt(digitCount.getText().toString()));
            passwordOptions.put("punctuation_count", Integer.parseInt(punctuationCount.getText().toString()));
            passwordOptions.put("length_min", Integer.parseInt(lengthMin.getText().toString()));
            passwordOptions.put("length_max", Integer.parseInt(lengthMax.getText().toString()));
        } catch (JSONException ex) {
            ex.getStackTrace();
        }

    	return generateDicewarePassword.newPassword(passwordOptions);
    }
}
