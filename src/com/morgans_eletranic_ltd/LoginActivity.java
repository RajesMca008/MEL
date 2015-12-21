package com.morgans_eletranic_ltd;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.morgans_eletranic_ltd.data.Data;

public class LoginActivity extends Activity {

	private EditText edtUid, edtPwd;
	private CheckBox cb;
	private Button btnLogin;
	private Activity act = LoginActivity.this;
	private boolean cbStatus = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initRef();
	}

	private void initRef() {
		edtUid = (EditText) findViewById(R.id.login_edt_username);
		edtPwd = (EditText) findViewById(R.id.login_edt_pswd);
		cb = (CheckBox) findViewById(R.id.login_checkbox_rem_me);
		btnLogin = (Button) findViewById(R.id.login_btn_login);
		cbStatus = Utils.fetchCbStatus(act);

		if (cbStatus) {
			cb.setChecked(true);
			edtUid.setText(Utils.fetchData(Constants.KEY_UID, act));
			edtPwd.setText(Utils.fetchData(Constants.KEY_PWD, act));
		}

		// login button listener
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				String uid = edtUid.getText().toString().trim();
				String pwd = edtPwd.getText().toString().trim();
				if (!TextUtils.isEmpty(uid)) {
					if (!TextUtils.isEmpty(pwd)) {
						try {
							Utils.insertCbData(Constants.KEY_CB, cbStatus, act);
							if (cbStatus) {
								Utils.insertData(Constants.KEY_UID, uid, act);
								Utils.insertData(Constants.KEY_PWD, pwd, act);
							}
							String userData = "Username=" + uid + "&Password="
									+ pwd;
							new LoginTask(userData).execute();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else
						Utils.showToast("plz enter password", act);
				} else
					Utils.showToast("plz enter Username", act);
			}
		});

		// check box listener
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				cbStatus = isChecked;
			}
		});
	}

	private class LoginTask extends AsyncTask<Void, String, String> {
		ProgressDialog pd = null;
		String response = null;
		JSONObject obj = null;
		private String userData = "";

		public LoginTask(String userData) {
			this.userData = userData;
		}

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(act, null, "login ..");
			pd.setCancelable(true);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				response = Data.getDataFromServer("", "POST",
						Constants.LOGIN_URL + userData, Constants.CONTENT_TYPE);
				if (!TextUtils.isEmpty(response))
					parseData(response);
			} catch (Exception e) {
			}

			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			pd.cancel();
			if (Constants.LOGIN_STATUS) {
				Utils.showToast("logged in Successfully", act);
				if (!cbStatus) {
					edtUid.setText("");
					edtPwd.setText("");
				}
				startActivity(new Intent(act, HomeActivity.class));
			} else {
				Utils.showToast("Login failed", act);
				edtUid.setText("");
				edtPwd.setText("");
			}
		}

		private void parseData(String jsonData) {
			try {
				JSONObject obj = new JSONObject(jsonData)
						.getJSONObject("validateUserCredentialsResult");
				String result = obj.getString("Result");
				if (result.equals("YES")) {
					Constants.LOGIN_STATUS = true;
					Constants.USER_ID = obj.getString("UserID");
					Constants.isSupervisor = obj.getBoolean("IsSupervisor");
					Constants.isEngineer = obj.getBoolean("IsEngineer");
				} else
					Constants.LOGIN_STATUS = false;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 
		 switch (keyCode) {    
	        case KeyEvent.KEYCODE_ENTER:
	           Toast.makeText(act, "TEST",Toast.LENGTH_LONG).show();
	        break;
	    }
		 
		return super.onKeyDown(keyCode, event);
	}
}
