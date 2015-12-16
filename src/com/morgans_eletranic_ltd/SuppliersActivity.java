package com.morgans_eletranic_ltd;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.morgans_eletranic_ltd.data.Data;
import com.morgans_eletranic_ltd.data.JobsData;
import com.morgans_eletranic_ltd.data.SuppliersObj;

public class SuppliersActivity extends BaseActivity implements OnClickListener {

	private ListView listView = null;
	private Activity act;
	private LinearLayout layout;

	@Override
	public void initControls() {
		act = SuppliersActivity.this;
		layout = (LinearLayout) inflater.inflate(R.layout.suppliers_view, null);
		llBody.addView(layout, baseLayoutParams);

		listView = (ListView) findViewById(R.id.homescreen_listview);
		
        final EditText searchView=(EditText)findViewById(R.id.supplier_search);
        
        ImageButton button=(ImageButton)findViewById(R.id.but_search);
        button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(act, "Clicked", Toast.LENGTH_LONG).show();
				
				if(searchView.getText().toString().trim().length()>0)
				{
				SupplierGetDataTask dataTask=new SupplierGetDataTask();
				dataTask.execute(searchView.getText().toString());
				}
				else{
					searchView.setError("Invalid input");
				}
				
			}
		});
		
	}
	
	
	class SupplierGetDataTask extends AsyncTask<String, Void, SuppliersObj>
	{
		ProgressDialog dialog=null;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog=new ProgressDialog(act);
			dialog.setTitle("Loading...");
			dialog.show();
		}

		@Override
		protected SuppliersObj doInBackground(String... params) {
			
			String response = Data.getDataFromServer("", "POST",
					Constants.SUPPLIERS_LIST+"="+params[0], Constants.CONTENT_TYPE);
			System.out.println("TESt :"+response);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(SuppliersObj result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			try{
				
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			dialog.cancel();
		}
	}
}