package com.morgans_eletranic_ltd;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.morgans_eletranic_ltd.data.Data;
import com.morgans_eletranic_ltd.data.JobFiles;
import com.morgans_eletranic_ltd.data.JobNotes;
import com.morgans_eletranic_ltd.data.JobsData;

public class JobDetailsActivity extends Activity implements OnClickListener {

	private EditText detail_description = null, short_description = null;
	private JobsData jobData = null;
	private ListView listView = null;
	private TextView tvName, tvAddr, tvPhno, tvSiteContact, tvJobID;
	private ArrayList<JobNotes> arrJobNotes = new ArrayList<JobNotes>();
	private ArrayList<JobFiles> arrJobFiles = new ArrayList<JobFiles>();

	private LinearLayout layout;
	private Button startJob, endJob, jobNotes, addNotes, viewFiles;
	private SharedPreferences pref;
	private Editor editor = null;
	ProgressDialog pd;
	Activity act = JobDetailsActivity.this;
	private Button bt_extras_and_amends;
	private Button bt_job_complete_reshedule;
	private Button bt_exit;
	private ToggleButton bt_certificate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_details);

		pref = getSharedPreferences("pref", MODE_PRIVATE);
		editor = pref.edit();
		initRef();
	}

	private void initRef() {
		jobData = Utils.getTodayJobsData();
		detail_description = (EditText) findViewById(R.id.edt_shot_job_desc); // Id changed 
		short_description = (EditText) findViewById( R.id.edt_job_desc);// Id changed 
		tvName = (TextView) findViewById(R.id.tvJobs_name);
		tvAddr = (TextView) findViewById(R.id.tvJobs_addr);
		tvPhno = (TextView) findViewById(R.id.tvJobs_phno);
		tvSiteContact = (TextView) findViewById(R.id.tvJobs_sitecontact);
		tvJobID = (TextView) findViewById(R.id.tvJobs_jobid);
		bt_extras_and_amends=(Button)findViewById(R.id.bt_extras_and_amends);
		bt_job_complete_reshedule=(Button)findViewById(R.id.bt_job_complete_reshedule);
		bt_exit=(Button)findViewById(R.id.bt_exit);
		bt_certificate=(ToggleButton)findViewById(R.id.bt_certificate);

		bt_job_complete_reshedule.setOnClickListener(this);
		bt_extras_and_amends.setOnClickListener(this);
		bt_exit.setOnClickListener(this);


		bt_certificate.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub

			}
		});


		tvName.setText(jobData.CustomerName);
		tvAddr.setText(jobData.SiteAddress);

		Button addEstimate=(Button)findViewById(R.id.add_estimate);

		addEstimate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(JobDetailsActivity.this,EstimateActivity.class);
				startActivity(intent);
			}
		});

		if (TextUtils.isEmpty(jobData.SiteContactPhone))
			tvPhno.setText("phone number not added");
		else
			tvPhno.setText("Site Phone No: " + jobData.SiteContactPhone);

		tvSiteContact.setText("SiteContact: " + jobData.SiteContact);
		tvJobID.setText("Job Number: " + jobData.JobNumber);

		if (jobData.JobDescription.equals("null"))
			detail_description.setText("");
		else
			detail_description.setText(jobData.JobDescription);

		if (jobData.BriefJobDescription.equals("null"))
			short_description.setText("");
		else
			short_description.setText(jobData.BriefJobDescription);

		startJob = (Button) findViewById(R.id.btn_jobdetails_startjob);
		endJob = (Button) findViewById(R.id.bt4);
		jobNotes = (Button) findViewById(R.id.button1);
		addNotes = (Button) findViewById(R.id.button2);
		viewFiles = (Button) findViewById(R.id.button3);

		Log.e("pref", pref.getBoolean("jobStarted" + jobData.JobID, false)
				+ ",,");

		if (pref.getBoolean("jobStarted" + jobData.JobID, false)) {
			startJob.getBackground().setAlpha(20);
			startJob.setClickable(false);
			startJob.setEnabled(false);

			endJob.getBackground().setAlpha(255);
			endJob.setClickable(true);
			endJob.setEnabled(true);
		} else {
			endJob.getBackground().setAlpha(20);
			endJob.setClickable(false);
			endJob.setEnabled(false);

			startJob.getBackground().setAlpha(255);
			startJob.setClickable(true);
			startJob.setEnabled(true);
		}

		startJob.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View arg0) {
				startJob.getBackground().setAlpha(50);
				startJob.setClickable(false);
				startJob.setEnabled(false);

				endJob.getBackground().setAlpha(255);
				endJob.setClickable(true);
				endJob.setEnabled(true);

				editor.putBoolean("jobStarted" + jobData.JobID, true);
				editor.commit();

				new StartOrEndJobTask(Constants.START_JOB + Constants.JOB_ID
						+ "&SupervisorID=" + Constants.USER_ID
						+ "&StartEndTime=" + Utils.getDate()).execute();
			}
		});

		endJob.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				endJob.getBackground().setAlpha(50);
				endJob.setClickable(false);
				endJob.setEnabled(false);

				startJob.getBackground().setAlpha(255);
				startJob.setClickable(true);
				startJob.setEnabled(true);

				editor.putBoolean("jobStarted" + jobData.JobID, false);
				editor.commit();

				new StartOrEndJobTask(Constants.END_JOB + Constants.JOB_ID
						+ "&SupervisorID=" + Constants.USER_ID
						+ "&StartEndTime=" + Utils.getDate()).execute();
			}
		});

		jobNotes.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View arg0) {
				new GetJobNotesTask().execute();
			}
		});

		addNotes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivityForResult(
						new Intent(act, JobsNotesActivity.class), 0);
			}
		});

		viewFiles.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//new GetJobFilesTask().execute();

				Intent intent=new Intent(JobDetailsActivity.this,AddJobFiles.class);
				startActivity(intent);
			}
		});

		listView = (ListView) findViewById(R.id.jobdetails_listview);

		((ImageView) findViewById(R.id.base_logo))
		.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(act, HomeActivity.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			}
		});

		new GetJobNotesTask().execute();
	}

	class ViewJobsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (arrJobNotes.size() > 0)
				return arrJobNotes.size();
			else
				return 0;
		}

		@Override
		public Object getItem(int position) {

			return position;
		}

		@Override
		public long getItemId(int position) {

			return 0;
		}

		@Override
		public View getView(int pos, View view, ViewGroup parent) {
			ViewHolder holder = null;
			JobNotes data = arrJobNotes.get(pos);

			if (view == null) {
				holder = new ViewHolder();
				view = getLayoutInflater().inflate(R.layout.jobs_list_style,
						null);
				holder.tvName = (TextView) view.findViewById(R.id.list_name);
				view.setTag(holder);
			} else
				holder = (ViewHolder) view.getTag();

			holder.tvName.setText(toRoman(pos + 1) + ") " + data.JobNotes);

			return view;
		}
	}

	class ViewFilesAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (arrJobFiles.size() > 0)
				return arrJobFiles.size();
			else
				return 0;
		}

		@Override
		public Object getItem(int position) {

			return position;
		}

		@Override
		public long getItemId(int position) {

			return 0;
		}

		@Override
		public View getView(int pos, View view, ViewGroup parent) {
			ViewHolder holder = null;
			JobFiles data = arrJobFiles.get(pos);

			if (view == null) {
				holder = new ViewHolder();
				view = getLayoutInflater().inflate(R.layout.jobs_list_style,
						null);
				holder.tvName = (TextView) view.findViewById(R.id.list_name);
				view.setTag(holder);
			} else
				holder = (ViewHolder) view.getTag();

			holder.tvName.setText(toRoman(pos + 1) + ") " + data.JobFileTitle);

			return view;
		}
	}

	static class ViewHolder {
		TextView tvName;
	}

	// async tasks

	private class GetJobNotesTask extends AsyncTask {

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(act, null, "Jobnotes fetching...");
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Object result) {
			pd.cancel();
			listView.setAdapter(new ViewJobsAdapter());
			super.onPostExecute(result);
		}

		@Override
		protected Object doInBackground(Object... params) {
			String response = "";
			try {
				response = Data.getDataFromServer("", "POST",
						Constants.VIEW_JOBS + Constants.JOB_ID,
						Constants.CONTENT_TYPE);
				if (!TextUtils.isEmpty(response))
					parseData(response);
			} catch (Exception e) {
			}
			return null;
		}

	}

	private void parseData(String jsonData) {
		JSONArray arr = null;
		JobNotes notes = null;
		try {
			arrJobNotes = null;
			arrJobNotes = new ArrayList<JobNotes>();
			arr = new JSONObject(jsonData).getJSONArray("viewJobNotesResult");
			for (int i = 0; i < arr.length(); i++) {
				notes = new JobNotes();
				notes.JobID = arr.getJSONObject(i).getString("JobID");
				notes.JobNotes = arr.getJSONObject(i).getString("JobNotes");
				notes.JobNotesAddedOn = arr.getJSONObject(i).getString(
						"JobNotesAddedOn");
				notes.JobNotesID = arr.getJSONObject(i).getString("JobNotesID");

				arrJobNotes.add(notes);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class GetJobFilesTask extends AsyncTask {

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(act, null, "Jobfiles fetching...");
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Object result) {
			pd.cancel();
			listView.setAdapter(new ViewFilesAdapter());
			super.onPostExecute(result);
		}

		@Override
		protected Object doInBackground(Object... params) {
			String response = "";
			try {
				response = Data.getDataFromServer("", "POST",
						Constants.VIEW_FILES + Constants.JOB_ID,
						Constants.CONTENT_TYPE);
				if (!TextUtils.isEmpty(response))
					parseData1(response);
			} catch (Exception e) {
			}
			return null;
		}

	}

	private void parseData1(String jsonData) {
		JSONArray arr = null;
		JobFiles file = null;
		try {
			arrJobFiles = null;
			arrJobFiles = new ArrayList<JobFiles>();
			arr = new JSONObject(jsonData).getJSONArray("viewJobFilesResult");
			for (int i = 0; i < arr.length(); i++) {
				file = new JobFiles();
				file.CreatedBy = arr.getJSONObject(i).getString("CreatedBy");
				file.CreatedOn = arr.getJSONObject(i).getString("CreatedOn");
				file.DeletedBy = arr.getJSONObject(i).getString("DeletedBy");
				file.DeletedOn = arr.getJSONObject(i).getString("DeletedOn");
				file.JobFile = arr.getJSONObject(i).getString("JobFile");
				file.JobFileID = arr.getJSONObject(i).getString("JobFileID");
				file.JobFileTitle = arr.getJSONObject(i).getString(
						"JobFileTitle");
				file.JobID = arr.getJSONObject(i).getString("JobID");
				file.UpdatedBy = arr.getJSONObject(i).getString("UpdatedBy");
				file.UpdatedOn = arr.getJSONObject(i).getString("UpdatedOn");
				arrJobFiles.add(file);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class StartOrEndJobTask extends AsyncTask {

		private String url;

		public StartOrEndJobTask(String url) {
			super();
			this.url = url;
		}

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(act, null, "Day status updated..");
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Object result) {
			pd.cancel();
			super.onPostExecute(result);
		}

		@Override
		protected Object doInBackground(Object... arg0) {
			Log.e("url", url);
			String res = Data.getDataFromServer("", "POST", url,
					Constants.CONTENT_TYPE);
			Log.e("res", res + "");
			return null;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			new GetJobNotesTask().execute();
		}
	}

	public String toRoman(int num) {

		String Roman = ""; // This will be our result string.

		// Declare and Initiate our Arrays
		String onesArray[] = { "I", "II", "III", "IV", "V", "VI", "VII",
				"VIII", "IX" };
		String tensArray[] = { "X", "XX", "XXX", "XL", "L", "LX", "LXX",
				"LXXX", "XC" };
		String hundredsArray[] = { "C", "CC", "CCC", "CD", "D", "DC", "DCC",
				"DCCC", "CM" };

		// Get the ones in the number
		int ones = num % 10;

		// Get the tens
		num = (num - ones) / 10;
		int tens = num % 10;

		// Get the hundreds
		num = (num - tens) / 10;
		int hundreds = num % 10;

		// Get and write the thousands in the number to our string

		num = (num - hundreds) / 10;

		for (int i = 0; i < num; i++) {
			Roman += "M";
		}

		// Write the hundreds

		if (hundreds >= 1) {
			Roman += hundredsArray[hundreds - 1];
		}

		// Write the tens
		if (tens >= 1) {
			Roman += tensArray[tens - 1];
		}

		// And finally, write the ones
		if (ones >= 1) {
			Roman += onesArray[ones - 1];
		}

		// Return our string.
		return String.valueOf(Roman);

	}

	@Override
	public void onClick(View v) {


		switch (v.getId()) {
		case R.id.bt_job_complete_reshedule:

			Intent intentReshedule=new Intent(JobDetailsActivity.this,JobReshedule.class);

			startActivity(intentReshedule);
			break;

		case R.id.bt_extras_and_amends:

			Intent intentExtras=new Intent(JobDetailsActivity.this,ExtrasAndAmends.class);

			startActivity(intentExtras);
			break;
		case R.id.bt_exit:
			finish();
			break;
		case R.id.job_details_view_materials:

			Intent intentMaterials=new Intent(JobDetailsActivity.this,MaterialsActivity.class);

			startActivity(intentMaterials);
			break;
		default:
			break;
		}

	}
}