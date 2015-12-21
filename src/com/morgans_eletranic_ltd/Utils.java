package com.morgans_eletranic_ltd;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import com.morgans_eletranic_ltd.data.JobsData;

public class Utils {

	public static void showToast(String mes, Activity act) {
		Toast.makeText(act, mes, 300).show();
	}

	public static void insertData(String key, String value, Activity act) {
		try {
			Editor editor = act.getSharedPreferences(Constants.PREF_NAME,
					act.MODE_PRIVATE).edit();
			editor.putString(key, value);
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void insertCbData(String key, boolean value, Activity act) {
		try {
			Editor editor = act.getSharedPreferences(Constants.PREF_NAME,
					act.MODE_PRIVATE).edit();
			editor.putBoolean(key, value).commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String fetchData(String key, Activity act) {
		String data = "";
		try {
			data = act.getSharedPreferences(Constants.PREF_NAME,
					act.MODE_PRIVATE).getString(key, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public static boolean fetchCbStatus(Activity act) {
		boolean b = false;
		try {
			b = act.getSharedPreferences(Constants.PREF_NAME, act.MODE_PRIVATE)
					.getBoolean(Constants.KEY_CB, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public static String getDate() {
		SimpleDateFormat sdf = null;
		try {
			sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sdf.format(System.currentTimeMillis());
	}

	public static String convertedDate(int day, int month, int year) {

		Calendar cal = null;
		long mills = 0;
		SimpleDateFormat dateFormat = null;

		try {
			cal = Calendar.getInstance();
			cal.setTimeInMillis(System.currentTimeMillis());
			cal.set(Calendar.DAY_OF_MONTH, day);
			cal.set(Calendar.MONTH, month);
			cal.set(Calendar.YEAR, year);
			mills = cal.getTimeInMillis();

			if (dateFormat == null) {
				dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateFormat.format(new Date(mills));
	}

	public static String convertedDate1(String d) {
		Date date = null;
		String strDate = "";
		SimpleDateFormat dateFormat1 = null, dateFormat2 = null;
		try {
			dateFormat1 = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
			dateFormat2 = new SimpleDateFormat("dd-MMMM-yyyy", Locale.UK);
			date = dateFormat1.parse(d);
			strDate = dateFormat2.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	public static String convertedDate2(String d) {
		Date date = null;
		String strDate = "";
		SimpleDateFormat dateFormat1 = null, dateFormat2 = null;
		try {
			dateFormat1 = new SimpleDateFormat("dd-MMMM-yyyy", Locale.UK);
			dateFormat2 = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
			date = dateFormat1.parse(d);
			strDate = dateFormat2.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	private static JobsData jobsData = null;
	private static ArrayList<JobsData> allJobs = null;
	private static String pickedDate = "";

	public static String getPickedDate() {
		return pickedDate;
	}

	public static void setPickedDate(String pickedDate) {
		Utils.pickedDate = pickedDate;
	}

	public static void setTodyJobsData(JobsData data) {
		jobsData = data;
	}

	public static ArrayList<JobsData> getAllJobs() {
		return allJobs;
	}

	public static void setAllJobs(ArrayList<JobsData> allJobs) {
		Utils.allJobs = allJobs;
	}

	public static JobsData getTodayJobsData() {
		return jobsData;
	}
}
