package com.morgans_eletranic_ltd;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.morgans_eletranic_ltd.data.Data;
import com.morgans_eletranic_ltd.data.JobsData;

public class SimpleCalendarViewActivity extends Activity implements
		OnClickListener {
	private static final String tag = "SimpleCalendarViewActivity";

	private Activity act = SimpleCalendarViewActivity.this;
	private Button selectedDayMonthYearButton;
	public Button currentMonth;
	private ImageView prevMonth;
	private ImageView nextMonth;
	private GridView calendarView;
	private GridCellAdapter adapter;
	private Calendar _calendar;
	private int month, year;
	@SuppressLint("NewApi")
	private final DateFormat dateFormatter = new DateFormat();
	private static final String dateTemplate = "MMMM yyyy";
	TextView hlyday, descr;
	public static boolean one = false;
	public static boolean two = false;
	public static boolean three = false;
	public static boolean four = false;
	public static boolean five = false;
	public static boolean six = false;
	public static boolean seven = false;
	public static boolean eight = false;
	public static boolean nine = false;
	public static boolean ten = false;
	public static boolean eleven = false;
	public static boolean twelve = false;
	public static boolean thirtn = false;
	public static boolean fourtn = false;
	public static boolean fiftn = false;
	public static boolean sixtn = false;
	public static boolean sevntn = false;
	public static boolean eightn = false;
	public static boolean nintn = false;
	public static boolean twenty = false;
	private ArrayList<JobsData> allJobs = null;
	private ArrayList<String> dates = new ArrayList<String>();
	private ListView listView = null;
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_calendar_view1);

		listView = (ListView) findViewById(R.id.simple_calender_listview);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				Utils.setTodyJobsData(jobsList.get(pos));
				Constants.JOB_ID = jobsList.get(pos).JobID;
				startActivity(new Intent(act, JobDetailsActivity.class));
			}
		});
		_calendar = Calendar.getInstance(Locale.getDefault());
		month = _calendar.get(Calendar.MONTH) + 1;
		year = _calendar.get(Calendar.YEAR);
		Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: "
				+ year);

		selectedDayMonthYearButton = (Button) this
				.findViewById(R.id.selectedDayMonthYear);
		selectedDayMonthYearButton.setText("Selected: ");

		prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(this);

		currentMonth = (Button) this.findViewById(R.id.currentMonth);
		currentMonth.setText(dateFormatter.format(dateTemplate,
				_calendar.getTime()));

		nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
		nextMonth.setOnClickListener(this);

		calendarView = (GridView) this.findViewById(R.id.calendar);

		allJobs = Utils.getAllJobs();
		for (int i = 0; i < allJobs.size(); i++) {
			dates.add(Utils.convertedDate1(allJobs.get(i).StartDate));
		}

		Set<String> hs = new HashSet<String>();
		hs.addAll(dates);
		dates.clear();
		dates.addAll(hs);

		// Initialised
		adapter = new GridCellAdapter(getApplicationContext(),
				R.id.calendar_day_gridcell, month, year);
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);

		((ImageView) findViewById(R.id.base_logo))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(act, HomeActivity.class)
								.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					}
				});
	}

	/**
	 * 
	 * @param month
	 * @param year
	 */
	@SuppressLint("NewApi")
	private void setGridCellAdapterToDate(int month, int year) {
		adapter = new GridCellAdapter(getApplicationContext(),
				R.id.calendar_day_gridcell, month, year);
		_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
		currentMonth.setText(DateFormat.format(dateTemplate,
				_calendar.getTime()));
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		if (v == prevMonth) {
			if (month <= 1) {
				month = 12;
				year--;
			} else {
				month--;
			}
			Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "
					+ month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
		}
		if (v == nextMonth) {
			if (month > 11) {
				month = 1;
				year++;
			} else {
				month++;
			}
			Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: "
					+ month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
		}

	}

	@Override
	public void onDestroy() {
		Log.d(tag, "Destroying View ...");
		super.onDestroy();
	}

	// Inner Class
	public class GridCellAdapter extends BaseAdapter implements OnClickListener {
		private static final String tag = "GridCellAdapter";
		private final Context _context;

		private final List<String> list;
		private static final int DAY_OFFSET = 1;
		private final String[] weekdays = new String[] { "Sun", "Mon", "Tue",
				"Wed", "Thu", "Fri", "Sat" };
		private final String[] months = { "January", "February", "March",
				"April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
				31, 30, 31 };
		private final int month, year;
		private int daysInMonth, prevMonthDays;
		public int currentDayOfMonth;
		public int currentWeekDay;
		private Button gridcell;
		private TextView num_events_per_day;
		private final HashMap eventsPerMonthMap;
		private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"dd-MMM-yyyy");

		// Days in Current Month
		public GridCellAdapter(Context context, int textViewResourceId,
				int month, int year) {
			super();
			this._context = context;
			this.list = new ArrayList<String>();
			this.month = month;
			this.year = year;

			Log.d(tag, "==> Passed in Date FOR Month: " + month + " "
					+ "Year: " + year);
			Calendar calendar = Calendar.getInstance();
			// int holyday_date = calendar.get(Calendar.DAY_OF_WEEK);
			setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
			setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));

			Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
			Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
			Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

			// Print Month
			printMonth(month, year);

			// Find Number of Events
			eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
		}

		private String getMonthAsString(int i) {
			return months[i];
		}

		private String getWeekDayAsString(int i) {
			return weekdays[i];
		}

		private int getNumberOfDaysOfMonth(int i) {
			return daysOfMonth[i];
		}

		public String getItem(int position) {
			return list.get(position);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		/**
		 * Prints Month
		 * 
		 * @param
		 * 
		 * @param yy
		 */
		private void printMonth(int mm, int yy) {
			Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
			// The number of days to leave blank at
			// the start of this month.
			int trailingSpaces = 0;
			int leadSpaces = 0;
			int daysInPrevMonth = 0;
			int prevMonth = 0;
			int prevYear = 0;
			int nextMonth = 0;
			int nextYear = 0;

			int currentMonth = mm - 1;
			String currentMonthName = getMonthAsString(currentMonth);
			daysInMonth = getNumberOfDaysOfMonth(currentMonth);

			Log.d(tag, "Current Month: " + " " + currentMonthName + " having "
					+ daysInMonth + " days.");

			// Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
			GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
			Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

			if (currentMonth == 11) {
				prevMonth = currentMonth - 1;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 0;
				prevYear = yy;
				nextYear = yy + 1;
				Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			} else if (currentMonth == 0) {
				prevMonth = 11;
				prevYear = yy - 1;
				nextYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 1;
				Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			} else {
				prevMonth = currentMonth - 1;
				nextMonth = currentMonth + 1;
				nextYear = yy;
				prevYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			}

			// Compute how much to leave before before the first day of the
			// month.
			// getDay() returns 0 for Sunday.
			int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
			trailingSpaces = currentWeekDay;

			Log.d(tag, "Week Day:" + currentWeekDay + " is "
					+ getWeekDayAsString(currentWeekDay));
			Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
			Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

			if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1) {
				++daysInMonth;
			}

			// Trailing Month days
			for (int i = 0; i < trailingSpaces; i++) {
				Log.d(tag,
						"PREV MONTH:= "
								+ prevMonth
								+ " => "
								+ getMonthAsString(prevMonth)
								+ " "
								+ String.valueOf((daysInPrevMonth
										- trailingSpaces + DAY_OFFSET)
										+ i));
				list.add(String
						.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
								+ i)
						+ "-GREY"
						+ "-"
						+ getMonthAsString(prevMonth)
						+ "-"
						+ prevYear);
			}

			// Current Month Days
			for (int i = 1; i <= daysInMonth; i++) {
				Log.d(currentMonthName, String.valueOf(i) + " "
						+ getMonthAsString(currentMonth) + " " + yy);
				int x = _calendar.get(Calendar.MONTH) + 1;
				Log.d(tag, "x " + x);
				Log.d(tag, "mm" + mm);

				if ((i == getCurrentDayOfMonth()) && (mm == x)) {
					list.add(String.valueOf(i) + "-BLUE" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				}

				else if ((x != prevMonth) && (x != nextMonth)) {
					list.add(String.valueOf(i) + "-WHITE" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else {
					list.add(String.valueOf(i) + "-WHITE" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				}
			}

			// Leading Month days
			for (int i = 0; i < list.size() % 7; i++) {
				Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
				list.add(String.valueOf(i + 1) + "-GREY" + "-"
						+ getMonthAsString(nextMonth) + "-" + nextYear);
			}
		}

		private HashMap findNumberOfEventsPerMonth(int year, int month) {
			HashMap map = new HashMap<String, Integer>();
			return map;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) _context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.calendar_day_gridcell, parent,
						false);
			}

			// Get a reference to the Day gridcell
			gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
			gridcell.setOnClickListener(this);

			// ACCOUNT FOR SPACING

			Log.d(tag, "Current Day: " + list.get(position));
			String[] day_color = list.get(position).split("-");
			String theday = day_color[0];
			String themonth = day_color[2];
			String theyear = day_color[3];
			if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
				if (eventsPerMonthMap.containsKey(theday)) {
					num_events_per_day = (TextView) row
							.findViewById(R.id.num_events_per_day);
					Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
					num_events_per_day.setText(numEvents.toString());
				}
			}

			// Set the Day GridCell

			gridcell.setTextColor(Color.RED);
			gridcell.setText(theday);
			gridcell.setTag(theday + "-" + themonth + "-" + theyear);
			Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-"
					+ theyear);

			if (day_color[1].equals("GREY")) {
				gridcell.setTextColor(Color.LTGRAY);
			}
			if (day_color[1].equals("WHITE")) {
				gridcell.setTextColor(Color.WHITE);
			}

			if (day_color[1].equals("BLUE")) {
				gridcell.setTextColor(getResources().getColor(R.color.static_text_color));
				gridcell.setTextSize(18f);
				gridcell.setTypeface(Typeface.DEFAULT_BOLD);
			}
			if (theday.equals(currentWeekDay) && !themonth.equals(currentMonth)) {
				gridcell.setTextColor(Color.WHITE);
			}

			 
			// if(theday.equals(c))

			for (int i = 0; i < dates.size(); i++) {
				String s = dates.get(i);
				String d = s.substring(0, 2); 
				String m = s.substring(3, s.lastIndexOf("-"));
				String y = s.substring(s.lastIndexOf("-") + 1, s.length());

				int day1 = Integer.parseInt(theday);
				int day2 = Integer.parseInt(d);
				if ((day1 == day2 && themonth.equals(m) && theyear.equals(y))) {
					gridcell.setTextColor(Color.parseColor("#DC099A"));
				}
				
				 
			}
			
			
			return row;
		}

		@Override
		public void onClick(View view) {
			String date_month_year = (String) view.getTag();
			selectedDayMonthYearButton.setText("Selected: " + date_month_year);
			Utils.setPickedDate(Utils.convertedDate2(date_month_year));

			new TodaysJobsTask().execute();

			try {
				Date parsedDate = dateFormatter.parse(date_month_year);
				Log.d(tag, "Parsed Date: " + parsedDate.toString());

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		public int getCurrentDayOfMonth() {
			return currentDayOfMonth;
		}

		private void setCurrentDayOfMonth(int currentDayOfMonth) {
			this.currentDayOfMonth = currentDayOfMonth;
		}

		public void setCurrentWeekDay(int currentWeekDay) {
			this.currentWeekDay = currentWeekDay;
		}

		public int getCurrentWeekDay() {
			return currentWeekDay;
		}
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
			date = Utils.getPickedDate();
			String res = Data.getDataFromServer("", "POST",
					Constants.JOBS_BY_DATE + "1&CurrentDate=" + date,
					Constants.CONTENT_TYPE);
			if (!TextUtils.isEmpty(res))
				parseData(res);
			return null;
		}

	}

	private void parseData(String data) {
		try {
			jobsList = new ArrayList<JobsData>();
			JSONArray arr = new JSONObject(data)
					.getJSONArray("viewSupervisorJobsByDateResult");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				JobsData jobsData = new JobsData();
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

	TextView blinkTV;

	ProgressDialog pd = null;
	private ArrayList<JobsData> jobsList = null;

}
