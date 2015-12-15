package com.morgans_eletranic_ltd;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.morgans_eletranic_ltd.data.Data;
import com.morgans_eletranic_ltd.data.JobsData;

public class HomeActivity extends BaseActivity implements OnClickListener {

	Button btnCalender, btnMaterials, btnSuppliers, btnMelforms, btnOthers,
			btnMore;
	String[] name;
	private Calendar calendar = null;
	private ArrayList<JobsData> jobsList = new ArrayList<JobsData>();
	private ListView listView = null;
	SimpleDateFormat simple_date_format;
	//TextView current_date_txt;
	private Activity act;
	private LinearLayout layout;
	private Button startDay, endDay;
	private SharedPreferences pref;
	private Editor editor;
	private ProgressDialog pd;
	TextView blinkTV;

	@Override
	public void initControls() {
		act = HomeActivity.this;
		layout = (LinearLayout) inflater.inflate(R.layout.home_screen, null);
		llBody.addView(layout, baseLayoutParams);

		//current_date_txt = (TextView) findViewById(R.id.home_txt_daytime);
		
		TextView week_day = (TextView) findViewById(R.id.week_day);
		TextView day_and_month = (TextView) findViewById(R.id.day_and_month);
		TextView text_time = (TextView) findViewById(R.id.text_time);
		 
		
		

		long date = System.currentTimeMillis();

		//simple_date_format = new SimpleDateFormat("MMM dd, yyyy h:mm a");
		simple_date_format = new SimpleDateFormat("E, dd, MMM, HH:mm");
		String dateString = simple_date_format.format(date);
		
		String dateSplit[]=dateString.split("\\,");
		
		//current_date_txt.setText(dateSplit[0]);
		
		week_day.setText(dateSplit[0]);
		
		day_and_month.setText(Html.fromHtml(dateSplit[1]+"<sup>th</sup>"+dateSplit[2]));
		text_time.setText(dateSplit[3]);

		listView = (ListView) findViewById(R.id.homescreen_listview);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				Utils.setTodyJobsData(jobsList.get(pos));
				Constants.JOB_ID = jobsList.get(pos).JobID;
				startActivity(new Intent(act, JobDetailsActivity.class));
			}
		});

		name = getResources().getStringArray(R.array.jobs_names);
		btnCalender = (Button) findViewById(R.id.homescreen_calender);
		btnCalender.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				new GetSupervisorAllJobsTask(act).execute();
			}
		});

		btnMaterials = (Button) findViewById(R.id.btn_materials);
		btnMaterials.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				startActivity(new Intent(act, MaterialsActivity.class));
			}
		});

		btnSuppliers = (Button) findViewById(R.id.button2);
		btnSuppliers.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(act, SuppliersActivity.class));
			}
		});

		btnMelforms = (Button) findViewById(R.id.button4);
		btnMelforms.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(act, MelFormsActivity.class));
			}
		});

		btnOthers = (Button) findViewById(R.id.button5);
		btnOthers.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(act, OthersActivity.class));
			}
		});

		btnMore = (Button) findViewById(R.id.button6);
		btnMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(act, MoreActivity.class));
			}
		});

		startDay = (Button) findViewById(R.id.home_btn_strdate);
		endDay = (Button) findViewById(R.id.home_btn_enddate);
		pref = getSharedPreferences("pref", MODE_PRIVATE);
		editor = pref.edit();

		if (pref.getBoolean("dayStarted" + Utils.getDate(), false)) {
			startDay.getBackground().setAlpha(50);
			startDay.setEnabled(false);
			startDay.setClickable(false);

			endDay.getBackground().setAlpha(255);
			endDay.setEnabled(true);
			endDay.setClickable(true);
		} else {
			endDay.getBackground().setAlpha(50);
			endDay.setEnabled(false);
			endDay.setClickable(false);

			startDay.getBackground().setAlpha(255);
			startDay.setEnabled(true);
			startDay.setClickable(true);
		}

		startDay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startDay.getBackground().setAlpha(50);
				startDay.setClickable(false);
				startDay.setClickable(false);

				endDay.getBackground().setAlpha(255);
				endDay.setClickable(true);
				endDay.setEnabled(true);

				editor.putBoolean("dayStarted" + Utils.getDate(), true);
				editor.commit();

				new StartOrEndDayTask(Constants.USER_ID + "&StartDayTime="
						+ Utils.getDate()).execute();
			}
		});

		endDay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				endDay.getBackground().setAlpha(50);
				endDay.setClickable(false);
				endDay.setEnabled(false);

				startDay.getBackground().setAlpha(255);
				startDay.setClickable(true);
				startDay.setClickable(true);

				editor.putBoolean("dayStarted" + Utils.getDate(), false);
				editor.commit();

				new StartOrEndDayTask(Constants.USER_ID + "&StartEndTime="
						+ Utils.getDate()).execute();
			}
		});

		new TodaysJobsTask(getIntent().getIntExtra("pos", 0)).execute();
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (jobsList.size() > 0)
				return jobsList.size();
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
			JobsData data = jobsList.get(pos);

			if (view == null) {
				holder = new ViewHolder();
				view = getLayoutInflater().inflate(R.layout.custom_listview,
						null);
				holder.tvName = (TextView) view.findViewById(R.id.list_name);
				holder.tvAddr = (TextView) view.findViewById(R.id.list_addr);
				holder.tvPhno = (TextView) view.findViewById(R.id.list_phno);
				holder.tvBlink = (TextView) view.findViewById(R.id.list_blink);
				
				holder.tvTime= (TextView) view.findViewById(R.id.tv_time);
				holder.tvJobID= (TextView) view.findViewById(R.id.tv_job_no);
				
				view.setTag(holder);
			} else
				holder = (ViewHolder) view.getTag();

			holder.tvName.setText(data.CustomerName);
			holder.tvAddr.setText(data.SiteAddress);
			holder.tvPhno.setText(data.SiteContactPhone);
			
			holder.tvTime.setText(data.StartDate);
			holder.tvJobID.setText(data.JobNumber);
			Log.e("cancel", data.IsCancellationFormRequired);
			if (data.IsCancellationFormRequired.equals("True")) {
				holder.tvBlink.setVisibility(View.VISIBLE);
				holder.tvBlink.setText("Cancel form");
			}
			return view;
		}
	}

	static class ViewHolder {
		TextView tvName;
		TextView tvAddr;
		TextView tvPhno;
		TextView tvBlink;
		
		TextView tvTime;
		TextView tvJobID;
	}

	private class TodaysJobsTask extends AsyncTask {

		private int pos = 0;
		private String date = "";

		public TodaysJobsTask(int pos) {
			super();
			this.pos = pos;
		}

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(act, null, "loading...");
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Object result) {
			pd.cancel();
			listView.setAdapter(new MyAdapter());
			super.onPostExecute(result);
		}

		@Override
		protected Object doInBackground(Object... arg0) {
			if (pos == 0)
				date = Utils.getDate();
			else
				date = Utils.getPickedDate();
			String res = Data.getDataFromServer("", "POST",
					Constants.JOBS_BY_DATE + Constants.USER_ID
							+ "&CurrentDate=" + date, Constants.CONTENT_TYPE);
			if (!TextUtils.isEmpty(res))
				parseData(res);
			return null;
		}

	}

	private void parseData(String data) {
		try {
			JSONArray arr = new JSONObject(data)
					.getJSONArray("viewSupervisorJobsByDateResult");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				JobsData jobsData = new JobsData();
				jobsData.BriefJobDescription = obj
						.getString("BriefJobDescription");
				jobsData.BusinessSource = obj.getString("BusinessSource");
				jobsData.BusinessSourceID = obj.getString("BusinessSourceID");
				jobsData.CreatedBy = obj.getString("CreatedBy");
				jobsData.CreatedOn = obj.getString("CreatedOn");
				jobsData.CurrentStatus = obj.getString("CurrentStatus");
				jobsData.CustomerID = obj.getString("CustomerID");
				jobsData.CustomerName = obj.getString("CustomerName");
				jobsData.DeletedBy = obj.getString("DeletedBy");
				jobsData.DeletedOn = obj.getString("DeletedOn");
				jobsData.EndDate = obj.getString("EndDate");
				jobsData.EngineerComments = obj.getString("EngineerComments");
				jobsData.EngineerID = obj.getString("EngineerID");
				jobsData.EngineerName = obj.getString("EngineerName");
				jobsData.HoldReason = obj.getString("HoldReason");
				jobsData.IsActive = obj.getString("IsActive");
				jobsData.IsCancellationFormRequired = obj
						.getString("IsCancellationFormRequired");
				jobsData.IsCompleted = obj.getString("IsCompleted");
				jobsData.IsStarted = obj.getString("IsStarted");
				jobsData.JobDescription = obj.getString("JobDescription");
				jobsData.JobID = obj.getString("JobID");
				jobsData.JobName = obj.getString("JobName");
				jobsData.JobNumber = obj.getString("JobNumber");
				jobsData.SiteAddress = obj.getString("SiteAddress");
				jobsData.SiteContact = obj.getString("SiteContact");
				jobsData.SiteContactPhone = obj.getString("SiteContactPhone");
				jobsData.SiteSurveyRequired = obj
						.getString("SiteSurveyRequired");
				jobsData.StartDate = obj.getString("StartDate");
				jobsData.SupervisorComments = obj
						.getString("SupervisorComments");
				jobsData.SupervisorID = obj.getString("SupervisorID");
				jobsData.SupervisorName = obj.getString("SupervisorName");
				jobsData.TypeofWork = obj.getString("TypeofWork");
				jobsData.UpdatedBy = obj.getString("UpdatedBy");
				jobsData.UpdatedOn = obj.getString("UpdatedOn");
				jobsData.OnHold = obj.getString("onHold");

				jobsList.add(jobsData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class StartOrEndDayTask extends AsyncTask {

		private String url;

		public StartOrEndDayTask(String url) {
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
			Log.e("url", Constants.START_DAY + url);
			String res = Data.getDataFromServer("", "POST", Constants.START_DAY
					+ url, Constants.CONTENT_TYPE);
			Log.e("res", res + "");
			return null;
		}

	}

	private class GetSupervisorAllJobsTask extends AsyncTask {

		private Context act = null;
		private ArrayList<JobsData> jobsList = new ArrayList<JobsData>();

		public GetSupervisorAllJobsTask(Context act) {
			super();
			this.act = act;
		}

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(act, null, null);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Object result) {
			pd.cancel();
			Utils.setAllJobs(jobsList);
			act.startActivity(new Intent(act, SimpleCalendarViewActivity.class));
			super.onPostExecute(result);
		}

		@Override
		protected Object doInBackground(Object... arg0) {
			String res = Data.getDataFromServer("", "POST",
					Constants.GET_ALL_JOBS + Constants.USER_ID,
					Constants.CONTENT_TYPE);
			if (!TextUtils.isEmpty(res))
				parseData(res);
			return null;
		}

		private void parseData(String data) {
			try {
				jobsList = null;
				jobsList = new ArrayList<JobsData>();
				JSONArray arr = new JSONObject(data)
						.getJSONArray("viewSupervisorAllJobsResult");
				for (int i = 0; i < arr.length(); i++) {
					JSONObject obj = arr.getJSONObject(i);
					JobsData jobsData = new JobsData();
					jobsData.BusinessSource = obj.getString("BusinessSource");
					jobsData.BusinessSourceID = obj
							.getString("BusinessSourceID");
					jobsData.CreatedBy = obj.getString("CreatedBy");
					jobsData.CreatedOn = obj.getString("CreatedOn");
					jobsData.CurrentStatus = obj.getString("CurrentStatus");
					jobsData.CustomerID = obj.getString("CustomerID");
					jobsData.CustomerName = obj.getString("CustomerName");
					jobsData.DeletedBy = obj.getString("DeletedBy");
					jobsData.DeletedOn = obj.getString("DeletedOn");
					jobsData.EndDate = obj.getString("EndDate");
					jobsData.EngineerComments = obj
							.getString("EngineerComments");
					jobsData.EngineerID = obj.getString("EngineerID");
					jobsData.EngineerName = obj.getString("EngineerName");
					jobsData.HoldReason = obj.getString("HoldReason");
					jobsData.IsActive = obj.getString("IsActive");
					jobsData.IsCancellationFormRequired = obj
							.getString("IsCancellationFormRequired");
					jobsData.IsCompleted = obj.getString("IsCompleted");
					jobsData.IsStarted = obj.getString("IsStarted");
					jobsData.JobDescription = obj.getString("JobDescription");
					jobsData.JobID = obj.getString("JobID");
					jobsData.JobName = obj.getString("JobName");
					jobsData.JobNumber = obj.getString("JobNumber");
					jobsData.SiteAddress = obj.getString("SiteAddress");
					jobsData.SiteContact = obj.getString("SiteContact");
					jobsData.SiteContactPhone = obj
							.getString("SiteContactPhone");
					jobsData.SiteSurveyRequired = obj
							.getString("SiteSurveyRequired");
					jobsData.StartDate = obj.getString("StartDate");
					jobsData.SupervisorComments = obj
							.getString("SupervisorComments");
					jobsData.SupervisorID = obj.getString("SupervisorID");
					jobsData.SupervisorName = obj.getString("SupervisorName");
					jobsData.TypeofWork = obj.getString("TypeofWork");
					jobsData.UpdatedBy = obj.getString("UpdatedBy");
					jobsData.UpdatedOn = obj.getString("UpdatedOn");
					jobsData.OnHold = obj.getString("onHold");

					jobsList.add(jobsData);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void blink() {
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				int timeToBlink = 500; // in milissegunds
				try {
					Thread.sleep(timeToBlink);
				} catch (Exception e) {
				}
				handler.post(new Runnable() {
					@Override
					public void run() {
						if (blinkTV.getVisibility() == View.VISIBLE) {
							blinkTV.setVisibility(View.INVISIBLE);
						} else {
							blinkTV.setVisibility(View.VISIBLE);
						}
						blink();
					}
				});
			}
		}).start();
	}
}