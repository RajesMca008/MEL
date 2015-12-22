package com.morgans_eletranic_ltd;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.morgans_eletranic_ltd.data.Data;
import com.morgans_eletranic_ltd.data.MaterialsData;

public class EstimateActivity  extends BaseActivity {

	//private Button btnSelectMeterials = null;
	private ArrayList<MaterialsData> arrMaterials = new ArrayList<MaterialsData>();
	private ArrayList<MaterialsData> addMaterialsList = new ArrayList<MaterialsData>();
	
	private ArrayList<LabourData> labourList = new ArrayList<LabourData>();
	private ArrayList<LabourData> addLabourList = new ArrayList<LabourData>();
	
	private String[] items = null, items1 = null;
	//private EditText edtMaterials = null;
	private ArrayList<String> listString = new ArrayList<String>();
	private String str = "";

	private RelativeLayout layout;

	private EditText seaerchMaterial=null;
	private ImageView searchButton=null;
	ListView materialListView=null;
	private MyMaterilaAdapter materialAdapter;
	private Spinner labourSpinner;
	
	private String laboutTypeSelected="";
	private ListView labourListView;
	private LabourAdapter labourAdapter;
	
	@Override
	public void initControls() {
		act = EstimateActivity.this;
		layout = (RelativeLayout) inflater.inflate(R.layout.job_estimate, null);
		llBody.addView(layout, baseLayoutParams);

		seaerchMaterial=(EditText)findViewById(R.id.material_search);
		searchButton=(ImageView) findViewById(R.id.but_search);
		
		labourSpinner=(Spinner)findViewById(R.id.sp_labourtype);
		
		
		materialListView=(ListView)findViewById(R.id.material_list_view);
		materialAdapter=new MyMaterilaAdapter();
		materialListView.setAdapter(materialAdapter);

		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new MaterialTask().execute(seaerchMaterial.getText().toString());

			}
		});
		
		
		labourListView=(ListView)findViewById(R.id.labor_list_view);
		labourAdapter=new LabourAdapter();
		labourListView.setAdapter(labourAdapter);
		

		
		labourSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				laboutTypeSelected= arg0.getItemAtPosition(arg2).toString();
				if(arg2!=0){
				Toast.makeText(act, "Selected:"+laboutTypeSelected, Toast.LENGTH_LONG).show();
				
				LabourDetailsTask detailsTask=new LabourDetailsTask();
				detailsTask.execute(laboutTypeSelected);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		materialListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Toast.makeText(act, "Menu",Toast.LENGTH_LONG).show();

				AlertDialog.Builder builder = new AlertDialog.Builder(act);


				final AlertDialog dialog = builder.create();

				dialog.setIcon(R.drawable.morgans_logo_icon);
				dialog.setTitle("Product details");
				View detailsView=View.inflate(act, R.layout.product_details, null);
				dialog.setView(detailsView);

				final EditText qty=(EditText)detailsView.findViewById(R.id.et_prod_qty);

				final EditText et_prod_margin=(EditText)detailsView.findViewById(R.id.et_prod_margin);
				String prodQty=((TextView)arg1.findViewById(R.id.prod_qty)).getText().toString();
				qty.setText(prodQty);

				et_prod_margin.setText(((MaterialsData)arg1.getTag()).Margin);


				Button but_update=(Button)detailsView.findViewById(R.id.but_update);
				but_update.setTag(arg1.getTag());
				Button but_delete=(Button)detailsView.findViewById(R.id.but_delete);
				Button but_cancel=(Button)detailsView.findViewById(R.id.but_cancel);

				but_update.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if(qty.getText().toString().length()>0 && et_prod_margin.getText().toString().length()>0)
						{
							((MaterialsData)v.getTag()).qty=qty.getText().toString();
							((MaterialsData)v.getTag()).Margin=et_prod_margin.getText().toString();
							//Toast.makeText(act, "TEST" +((MaterialsData)v.getTag()).Productname, Toast.LENGTH_LONG).show();

							materialAdapter.notifyDataSetChanged();
							dialog.dismiss();
						}
						else{
							if(qty.getText().toString().length()==0)
								qty.setError("Invalid");
							if(et_prod_margin.getText().toString().length()==0)
								et_prod_margin.setError("Invalid");
						}

					}
				});
				but_delete.setTag(arg1.getTag());
				but_delete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						addMaterialsList.remove(((MaterialsData)v.getTag()).index);
						materialAdapter.notifyDataSetChanged();
						dialog.dismiss();

					}
				});
				but_cancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

				dialog.show();

				return false;
			}
		});
	}
	
	class LabourAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return addLabourList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return addLabourList.get(position);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			View view=null;
			if(convertView ==null)
			{
				view = getLayoutInflater().inflate(R.layout.labour_estimate_header,
						null);

				view.setTag(addLabourList.get(position));

			}
			else{
				view=convertView;
			}
			
			TextView labourType=(TextView)view.findViewById(R.id.tv_labour_type);
			TextView tv__labour_price_type=(TextView)view.findViewById(R.id.tv__labour_price_type);
			TextView tv__labour_qty=(TextView)view.findViewById(R.id.tv__labour_qty);
			TextView tv_labour_cost_per_day_hour=(TextView)view.findViewById(R.id.tv_labour_cost_per_day_hour);
			TextView tv_labour_margin=(TextView)view.findViewById(R.id.tv_labour_margin);
			TextView tv_labour_margin_per_day_hour=(TextView)view.findViewById(R.id.tv_labour_margin_per_day_hour);
			TextView tv_labour_total_per_day_hour=(TextView)view.findViewById(R.id.tv_labour_total_per_day_hour);
			
			labourType.setText(addLabourList.get(position).getLabour());
			tv__labour_price_type.setText(addLabourList.get(position).getLabourType());
			tv__labour_qty.setText(addLabourList.get(position).getQty());
			tv_labour_cost_per_day_hour.setText(addLabourList.get(position).getPricePerDay()+"/hour \n"+addLabourList.get(position).getPricePerDay()+"/day");
			
			//Invalid values
			tv_labour_margin.setText(""+addLabourList.get(position).getMargin());
			tv_labour_margin_per_day_hour.setText(""+(Integer.parseInt(addLabourList.get(position).getPricePerDay())*addLabourList.get(position).getMargin())/100+"/day"+"\n"+(Integer.parseInt(addLabourList.get(position).getPricePerHour())*addLabourList.get(position).getMargin())/100+"/hour");
			tv_labour_total_per_day_hour.setText(""+addLabourList.get(position).getMargin());
			
			return view;
		}
		
	}
 

	class MyMaterilaAdapter extends BaseAdapter
	{

		public MyMaterilaAdapter() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return addMaterialsList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return addMaterialsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			View view=null;
			if(convertView ==null)
			{
				view = getLayoutInflater().inflate(R.layout.estimated_item_view,
						null);

				view.setTag(addMaterialsList.get(position));

			}
			else{
				view=convertView;
			}

			TextView prod_name=(TextView)view.findViewById(R.id.prod_name);
			TextView prod_price=(TextView)view.findViewById(R.id.prod_price);
			TextView prod_qty=(TextView)view.findViewById(R.id.prod_qty);
			TextView prod_margin=(TextView)view.findViewById(R.id.prod_margin);
			TextView prod_salePrice=(TextView)view.findViewById(R.id.prod_salePrice);
			TextView prod_total=(TextView)view.findViewById(R.id.prod_total);

			prod_name.setText(addMaterialsList.get(position).Productname);
			prod_price.setText("£"+addMaterialsList.get(position).CostPrice);
			prod_qty.setText(addMaterialsList.get(position).qty);//Default int
			prod_margin.setText(""+addMaterialsList.get(position).Margin);
			prod_salePrice.setText(""+addMaterialsList.get(position).MarginPrice);
			prod_total.setText(""+(Float.parseFloat(prod_qty.getText().toString())*(Float.parseFloat(addMaterialsList.get(position).CostPrice))+Float.parseFloat(addMaterialsList.get(position).MarginPrice)));
			addMaterialsList.get(position).index=position;
			return view;
		}

	}
	
	
	private class LabourDetailsTask extends AsyncTask<String, String, String> 
	{
		ProgressDialog pd = null;
		String response = null;
		
		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(act, null, null);
			pd.setCancelable(true);
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... params) {

			response = Data.getDataFromServer("", "POST",
					Constants.VIEW_LABOURS+"="+params[0], Constants.CONTENT_TYPE);
			if (!TextUtils.isEmpty(response))
				parseLabourData(response);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pd.dismiss();
			
			
			items = new String[labourList.size()];
			boolArray = new boolean[labourList.size()];
			for (int i = 0; i < boolArray.length; i++) {
				boolArray[i] = false;
			}
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < labourList.size(); i++)
				list.add(labourList.get(i).getLabourType());
			list.toArray(items);
			
			
			showLabourListUI();
			 
		}
		
		

		public void showLabourListUI() {


			listString.clear();
			listString = new ArrayList<String>();
			for (int i = 0; i < items.length; i++) {

				listString.add(items[i]);
			}

			items1 = null;
			items1 = new String[listString.size()];
			boolArray1 = new boolean[listString.size()];
			for (int j = 0; j < listString.size(); j++) {
				items1[j] = listString.get(j);
			}

			for (int j = 0; j < listString.size(); j++) {
				boolArray1[j] = false;
			}

			AlertDialog.Builder alert = new AlertDialog.Builder(act);
			alert.setTitle("Select Labour");
			alert.setSingleChoiceItems(items1, -1, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					for (int i = 0; i < boolArray1.length; i++) {
						boolArray1[i]=false;
					}
					boolArray1[which] = true;
				}
			});
			/*alert.setMultiChoiceItems(items1, boolArray1,
					new DialogInterface.OnMultiChoiceClickListener() {

				@Override
				public void onClick(DialogInterface dialog,
						int pos, boolean isChecked) {
					if (isChecked)
						boolArray1[pos] = true;
					else
						boolArray1[pos] = false;
				}
			});*/
			alert.setPositiveButton("ADD LABOUR",
					new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int pos) {

					//addMaterialsList.clear();
					for (int i = 0; i < boolArray1.length; i++) {
						if (boolArray1[i] == true) {
							//str = str + items1[i] + "\n";

							addLabourList.add(labourList.get(i));
						}
					}
					labourAdapter.notifyDataSetChanged();
					//Toast.makeText(act, "Selected itesm"+addMaterialsList.size(), Toast.LENGTH_LONG).show();
					Log.e("str", str);
					//edtMaterials.setText(str);
				}
			});
			alert.show();
		}
		
		
		private void parseLabourData(String jsonData) {
			JSONObject obj = null;
			LabourData data = null;
			try {
				arrMaterials = null;
				arrMaterials = new ArrayList<MaterialsData>();
				JSONArray arr = new JSONObject(jsonData)
				.getJSONArray("viewLaboursResult");
				for (int i = 0; i < arr.length(); i++) {
					obj = arr.getJSONObject(i);
					data = new LabourData();
					
					if(!obj.getString("CreatedBy").equals("null"))
					data.setCreatedBy(obj.getString("CreatedBy"));
					
					if(!obj.getString("CreatedOn").equals("null"))
					data.setCreatedOn(obj.getString("CreatedOn"));
					
					if(!obj.getString("Labour").equals("null"))
					data.setLabour(obj.getString("Labour"));
					
					if(!obj.getString("LabourType").equals("null"))
					data.setLabourType(obj.getString("LabourType"));
					
					if(!obj.getString("LabourCostID").equals("null"))
					data.setLabourCostID(obj.getString("LabourCostID"));
					
					if(!obj.getString("PricePerDay").equals("null"))
					data.setPricePerDay(obj.getString("PricePerDay"));
					
					if(!obj.getString("PricePerHour").equals("null"))
					data.setPricePerHour(obj.getString("PricePerHour"));
					
					if(!obj.getString("UpdatedBy").equals("null"))
					data.setUpdatedBy(obj.getString("UpdatedBy"));
					
					if(!obj.getString("UpdatedOn").equals("null"))
					data.setUpdatedOn(obj.getString("UpdatedOn"));
					
					 

					labourList.add(data);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
	}


	private class MaterialTask extends AsyncTask<String, String, String> {
		ProgressDialog pd = null;
		String response = null;
		JSONObject obj = null;
		private String userData = "";

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(act, null, null);
			pd.setCancelable(true);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				response = Data.getDataFromServer("", "POST",
						Constants.MATERIALS_LIST+"="+params[0], Constants.CONTENT_TYPE);
				if (!TextUtils.isEmpty(response))
					parseData(response);
			} catch (Exception e) {
			}

			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			items = new String[arrMaterials.size()];
			boolArray = new boolean[arrMaterials.size()];
			for (int i = 0; i < boolArray.length; i++) {
				boolArray[i] = false;
			}
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < arrMaterials.size(); i++)
				list.add(arrMaterials.get(i).Productname+" Price: $"+arrMaterials.get(i).CostPrice);
			list.toArray(items);
			pd.cancel();
			showItemListUI();
		}

		private void parseData(String jsonData) {
			JSONObject obj = null;
			MaterialsData data = null;
			try {
				arrMaterials = null;
				arrMaterials = new ArrayList<MaterialsData>();
				JSONArray arr = new JSONObject(jsonData)
				.getJSONArray("searchProductsResult");
				for (int i = 0; i < arr.length(); i++) {
					obj = arr.getJSONObject(i);
					data = new MaterialsData();
					data.CategoryID = obj.getString("CategoryID");
					data.CostPrice = obj.getString("CostPrice");
					data.CreatedBy = obj.getString("CreatedBy");
					data.CreatedOn = obj.getString("CreatedOn");
					data.DeletedBy = obj.getString("DeletedBy");
					data.DeletedOn = obj.getString("DeletedOn");
					data.Description = obj.getString("Description");
					data.IsActive = obj.getString("IsActive");
					data.ManufacturerCode = obj.getString("ManufacturerCode");
					data.ManufacturerType = obj.getString("ManufacturerType");
					data.Margin = obj.getString("Margin");
					data.MarginPrice = obj.getString("MarginPrice");
					data.PackSize = obj.getString("PackSize");
					data.ProductCode = obj.getString("ProductCode");
					data.ProductID = obj.getString("ProductID");
					data.Productname = obj.getString("Productname");
					data.SellingPrice = obj.getString("SellingPrice");
					data.SubCategoryID = obj.getString("SubCategoryID");
					data.SupplierID = obj.getString("SupplierID");
					data.UpdatedBy = obj.getString("UpdatedBy");
					data.UpdatedOn = obj.getString("UpdatedOn");
					data.WarrentyTerm = obj.getString("WarrentyTerm");

					arrMaterials.add(data);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	ArrayList<String> data = null;
	boolean[] boolArray = null, boolArray1 = null;
	Activity act = EstimateActivity.this;

	public void showItemListUI() {


		listString.clear();
		listString = new ArrayList<String>();
		for (int i = 0; i < items.length; i++) {

			listString.add(items[i]);
		}

		items1 = null;
		items1 = new String[listString.size()];
		boolArray1 = new boolean[listString.size()];
		for (int j = 0; j < listString.size(); j++) {
			items1[j] = listString.get(j);
		}

		for (int j = 0; j < listString.size(); j++) {
			boolArray1[j] = false;
		}

		AlertDialog.Builder alert = new AlertDialog.Builder(act);
		alert.setTitle("Select Your Materials");
		alert.setSingleChoiceItems(items1, -1, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				for (int i = 0; i < boolArray1.length; i++) {
					boolArray1[i]=false;
				}
				boolArray1[which] = true;
			}
		});
		/*alert.setMultiChoiceItems(items1, boolArray1,
				new DialogInterface.OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int pos, boolean isChecked) {
				if (isChecked)
					boolArray1[pos] = true;
				else
					boolArray1[pos] = false;
			}
		});*/
		alert.setPositiveButton("ADD ITEM",
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int pos) {

				//addMaterialsList.clear();
				for (int i = 0; i < boolArray1.length; i++) {
					if (boolArray1[i] == true) {
						//str = str + items1[i] + "\n";

						addMaterialsList.add(arrMaterials.get(i));
					}
				}
				materialAdapter.notifyDataSetChanged();
				//Toast.makeText(act, "Selected itesm"+addMaterialsList.size(), Toast.LENGTH_LONG).show();
				Log.e("str", str);
				//edtMaterials.setText(str);
			}
		});
		alert.show();
	}


}
