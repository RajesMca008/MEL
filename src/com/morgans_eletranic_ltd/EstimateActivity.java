package com.morgans_eletranic_ltd;

import java.util.ArrayList;

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
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.morgans_eletranic_ltd.data.Data;
import com.morgans_eletranic_ltd.data.MaterialsData;

public class EstimateActivity  extends BaseActivity {

	//private Button btnSelectMeterials = null;
	private ArrayList<MaterialsData> arrMaterials = new ArrayList<MaterialsData>();
	private ArrayList<MaterialsData> addMaterialsList = new ArrayList<MaterialsData>();
	private String[] items = null, items1 = null;
	private boolean[] bools = null;
	//private EditText edtMaterials = null;
	private ArrayList<String> listString = new ArrayList<String>();
	private EditText edtSearch;
	private String str = "";

	private RelativeLayout layout;

	private EditText seaerchMaterial=null;
	private ImageView searchButton=null;
	ListView materialListView=null;
	private MyMaterilaAdapter materialAdapter;
	@Override
	public void initControls() {
		act = EstimateActivity.this;
		layout = (RelativeLayout) inflater.inflate(R.layout.job_estimate, null);
		llBody.addView(layout, baseLayoutParams);

		seaerchMaterial=(EditText)findViewById(R.id.material_search);
		searchButton=(ImageView) findViewById(R.id.but_search);
		
		materialListView=(ListView)findViewById(R.id.material_list_view);
		materialAdapter=new MyMaterilaAdapter();
		materialListView.setAdapter(materialAdapter);

		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new MaterialTask().execute(seaerchMaterial.getText().toString());

			}
		});

		
		materialListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Toast.makeText(act, "Menu",Toast.LENGTH_LONG).show();
				
				AlertDialog.Builder alert = new AlertDialog.Builder(act);
				
				alert.setTitle("Product details");
				alert.setView(View.inflate(act, R.layout.product_details, null));
				
				alert.show();
				return false;
			}
		});
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
			prod_qty.setText("1");//Default int
			prod_margin.setText(""+addMaterialsList.get(position).Margin);
			prod_salePrice.setText(""+addMaterialsList.get(position).MarginPrice);
			prod_total.setText(""+(Float.parseFloat(prod_qty.getText().toString())*(Float.parseFloat(addMaterialsList.get(position).CostPrice))+Float.parseFloat(addMaterialsList.get(position).MarginPrice)));
			return view;
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
						Constants.MATERIALS_LIST/*+"="+params[0]*/, Constants.CONTENT_TYPE);
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
		alert.setMultiChoiceItems(items1, boolArray1,
				new DialogInterface.OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int pos, boolean isChecked) {
				if (isChecked)
					boolArray1[pos] = true;
				else
					boolArray1[pos] = false;
			}
		});
		alert.setPositiveButton("ADD ITEMS",
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
				Toast.makeText(act, "Selected itesm"+addMaterialsList.size(), Toast.LENGTH_LONG).show();
				Log.e("str", str);
				//edtMaterials.setText(str);
			}
		});
		alert.show();
	}


}
