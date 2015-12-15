package com.morgans_eletranic_ltd;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.morgans_eletranic_ltd.data.Data;
import com.morgans_eletranic_ltd.data.JobsData;

public class GetSupervisorAllJobsTask extends AsyncTask {

	private Context act = null;
	ProgressDialog pd = null;
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
		String res = Data.getDataFromServer("", "POST", Constants.GET_ALL_JOBS
				+ Constants.USER_ID, Constants.CONTENT_TYPE);
		if (!TextUtils.isEmpty(res))
			parseData(res);
		return null;
	}

	private void parseData(String data) {
		try {
			JSONArray arr = new JSONObject(data)
					.getJSONArray("viewSupervisorAllJobsResult");
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

}
