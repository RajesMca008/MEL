package com.morgans_eletranic_ltd;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.morgans_eletranic_ltd.data.Data;
import com.morgans_eletranic_ltd.data.MaterialsData;

public class MaterialsActivity extends BaseActivity {

	private Button btnSelectMeterials = null;
	private ArrayList<MaterialsData> arrMaterials = new ArrayList<MaterialsData>();
	private String[] items = null, items1 = null;
	private boolean[] bools = null;
	private EditText edtMaterials = null;
	private SearchView searchView;
	private ArrayList<String> listString = new ArrayList<String>();
	private EditText edtSearch;
	private String str = "";

	private LinearLayout layout;

	@Override
	public void initControls() {
		act = MaterialsActivity.this;
		layout = (LinearLayout) inflater.inflate(R.layout.materials, null);
		llBody.addView(layout, baseLayoutParams);

		initRef();
	}

	private void initRef() {
		new MaterialTask().execute();
		edtMaterials = (EditText) findViewById(R.id.edt_addmaterials);
		btnSelectMeterials = (Button) findViewById(R.id.btn_select_materials);
		btnSelectMeterials.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				data = new ArrayList<String>();
				AlertDialog.Builder alert = new AlertDialog.Builder(act);
				alert.setTitle("Select Your Materials");
				alert.setMultiChoiceItems(items, boolArray,
						new DialogInterface.OnMultiChoiceClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int pos, boolean isChecked) {
								if (isChecked)
									boolArray[pos] = true;
								else
									boolArray[pos] = false;
							}
						});
				alert.setPositiveButton("ADD ITEMS",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int pos) {
								for (int i = 0; i < boolArray.length; i++) {
									if (boolArray[i] == true) {
										data.add(items[i]);
										str = str + items[i] + "\n";
									}
								}
								edtMaterials.setText(str);
							}
						});
				alert.show();
			}
		});

		edtSearch = (EditText) findViewById(R.id.material_search_edt);
		edtSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				listString.clear();
				listString = new ArrayList<String>();
				for (int i = 0; i < items.length; i++) {
					if (items[i].toLowerCase(Locale.UK).contains(
							s.toString().toLowerCase(Locale.UK))) {
						listString.add(items[i]);
					}
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
								for (int i = 0; i < boolArray1.length; i++) {
									if (boolArray1[i] == true) {
										str = str + items1[i] + "\n";
									}
								}
								Log.e("str", str);
								edtMaterials.setText(str);
							}
						});
				alert.show();
			}
		});

		searchView = (SearchView) findViewById(R.id.material_search);
	}

	private class MaterialTask extends AsyncTask<Void, String, String> {
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
		protected String doInBackground(Void... params) {
			try {
				response = Data.getDataFromServer("", "POST",
						Constants.MATERIALS_LIST, Constants.CONTENT_TYPE);
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
				list.add(arrMaterials.get(i).Productname);
			list.toArray(items);
			pd.cancel();
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
	Activity act = MaterialsActivity.this;
}