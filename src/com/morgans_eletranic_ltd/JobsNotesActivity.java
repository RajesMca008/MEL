package com.morgans_eletranic_ltd;

import java.net.URLEncoder;

import com.morgans_eletranic_ltd.data.Data;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class JobsNotesActivity extends Activity {

	private Activity act;
	private Button btnUpload;
	private EditText edtNotes;
	private Intent intent;
	private String data = "", res = "";
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_notes);

		act = JobsNotesActivity.this;
		intent = getIntent();

		edtNotes = (EditText) findViewById(R.id.editText1);
		btnUpload = (Button) findViewById(R.id.button1);
		btnUpload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (!TextUtils.isEmpty(edtNotes.getText().toString().trim())) {
					data = edtNotes.getText().toString().trim();
					new JobNotesUploadTask().execute();
				} else
					Utils.showToast("plz enter notes", act);
			}
		});
	}

	private class JobNotesUploadTask extends AsyncTask {

		String url = "";

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(act, null, "job notes uploding...");
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Object result) {
			pd.cancel();
			Utils.showToast("Jobs notes saved successfully", act);
			setResult(RESULT_OK, intent);
			finish();
			super.onPostExecute(result);
		}

		@Override
		protected Object doInBackground(Object... arg0) {
			url = Constants.UPLOAD_NOTES + "JobID=" + Constants.JOB_ID
					+ "&JobNotes=" + URLEncoder.encode(data) + "&CurrentDate="
					+ Utils.getDate();
			res = Data.getDataFromServer("", "POST", url,
					Constants.CONTENT_TYPE);
			return null;
		}

	}
}