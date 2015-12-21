package com.morgans_eletranic_ltd;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.morgans_eletranic_ltd.data.Data;
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
        
        ImageView button=(ImageView)findViewById(R.id.but_search);
        button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
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
        
        
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				 Toast.makeText(act, "Test "+((SuppliersObj)arg0.getItemAtPosition(arg2)).getSupplierName(), Toast.LENGTH_LONG).show();
				 
				 
				// custom dialog
					final Dialog dialog = new Dialog(act);
					dialog.setContentView(R.layout.supplier_item_details);
					dialog.setTitle("Supplier Details");
					dialog.getWindow().setTitleColor(Color.WHITE);
					dialog.getWindow().setBackgroundDrawableResource(R.color.text_blue);
					 

					// set the custom dialog components - text, image and button
					TextView supplier_id = (TextView) dialog.findViewById(R.id.supplier_id);
					supplier_id.setText(((SuppliersObj)arg0.getItemAtPosition(arg2)).getSupplierID());
					
					
					TextView contactName = (TextView) dialog.findViewById(R.id.contact_name);
					contactName.setText(((SuppliersObj)arg0.getItemAtPosition(arg2)).getContactName());
					
					
					TextView email_addr = (TextView) dialog.findViewById(R.id.email_addr);
					email_addr.setText(((SuppliersObj)arg0.getItemAtPosition(arg2)).getEmail());
					
					TextView supplier_name = (TextView) dialog.findViewById(R.id.supplier_name);
					supplier_name.setText(((SuppliersObj)arg0.getItemAtPosition(arg2)).getSupplierName());
				
					
					TextView telephone = (TextView) dialog.findViewById(R.id.telephone);
					telephone.setText(((SuppliersObj)arg0.getItemAtPosition(arg2)).getTelephone());
				
					
					 
					Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
					// if button is clicked, close the custom dialog
					dialogButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});

					dialog.show();
					
				
			}
		});
		
	}
	
	
	class SupplierGetDataTask extends AsyncTask<String, Void, ArrayList<SuppliersObj>>
	{
		
		ArrayList<SuppliersObj> arrayList=null;
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
		protected ArrayList<SuppliersObj> doInBackground(String... params) {
			
			arrayList=new ArrayList<SuppliersObj>();
			String response = Data.getDataFromServer("", "POST",
					Constants.SUPPLIERS_LIST+"="+params[0], Constants.CONTENT_TYPE);
			
			try {
				JSONObject jsonObject=new JSONObject(response);
				
				System.out.println("TESt :"+jsonObject.getJSONArray("searchSuppliersBySupplierNameResult").length());
				
				
				SuppliersObj supplierObj=null;
				for (int i = 0; i < jsonObject.getJSONArray("searchSuppliersBySupplierNameResult").length(); i++) {
					
					supplierObj=new SuppliersObj();
					JSONObject jsonObject2= jsonObject.getJSONArray("searchSuppliersBySupplierNameResult").getJSONObject(i);
					supplierObj.setContactName((jsonObject2.getString("ContactName1").trim().length()==0)? "---": jsonObject2.getString("ContactName1").trim());
					supplierObj.setSupplierName(jsonObject2.getString("SupplierName"));
					supplierObj.setTelephone(jsonObject2.getString("Telephone"));
					supplierObj.setEmail(jsonObject2.getString("Email"));
					supplierObj.setSupplierID(jsonObject2.getString("SupplierID"));
					arrayList.add(supplierObj);
					 
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
			
			return arrayList;
		}
		
		@Override
		protected void onPostExecute(ArrayList<SuppliersObj> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			try{
				if(result!=null)
				{
					listView.setAdapter(new MyAdapter(result));
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			dialog.cancel();
		}
	}
	
	
	 
	class MyAdapter extends BaseAdapter {

		ArrayList<SuppliersObj> result =null;
		public MyAdapter(ArrayList<SuppliersObj> result) {
			this.result=result;
		}

		@Override
		public int getCount() {
			 
				return result.size();
		}

		@Override
		public Object getItem(int position) {

			return result.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int pos, View view, ViewGroup parent) {
			ViewHolder holder = null;
			 
 
			if (view == null) {
				holder = new ViewHolder();
				view = getLayoutInflater().inflate(R.layout.suppliery_list_item,
						null);
				holder.spContact = (TextView) view.findViewById(R.id.contact_name);
				holder.spName = (TextView) view.findViewById(R.id.supplier_name);
				holder.spEmail = (TextView) view.findViewById(R.id.email_addr);
				holder.spTelephone = (TextView) view.findViewById(R.id.telephone);
				 
			
				 
				view.setTag(holder);
			} else
				holder = (ViewHolder) view.getTag();

			holder.spContact.setText(Html.fromHtml("<b>Contact Name : </b>"+result.get(pos).getContactName()));
			   holder.spName.setText(Html.fromHtml("<b>Supplier Name: </b>"+result.get(pos).getSupplierName()));
			  holder.spEmail.setText(Html.fromHtml("<b>Email       : </b>"+result.get(pos).getEmail()));
		  holder.spTelephone.setText(Html.fromHtml("<b>Telephone   : </b>"+result.get(pos).getTelephone()));
			 
		 
			return view;
		}
	}

	static class ViewHolder {
		TextView spId;
		TextView spName;
		TextView spEmail;
		TextView spTelephone;
		TextView spContact;
	}
}