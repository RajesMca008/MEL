package com.morgans_eletranic_ltd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class BaseActivity extends Activity implements OnClickListener {
	public LinearLayout llLeftMenu, llBody, llGroup, llTop;
	public ImageView ivMenu, ivLogo;
	public TextView tv1, tv2, tv3, tv4, tv5, tv6;
	public static Context mContext;
	public LayoutInflater inflater;
	public DrawerLayout dLayout;
	public DrawerLayout.LayoutParams drawerParams;
	public ViewGroup.LayoutParams baseLayoutParams;
	private boolean enableDrawer = true;

	public abstract void initControls();

	public BaseActivity() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_view);
		initBaseControls();
		setBaseClickListeners();
		initControls();
	}

	private void initBaseControls() {
		mContext = BaseActivity.this;
		inflater = this.getLayoutInflater();
		baseLayoutParams = new ViewGroup.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		drawerParams = new DrawerLayout.LayoutParams(
				DrawerLayout.LayoutParams.MATCH_PARENT,
				DrawerLayout.LayoutParams.MATCH_PARENT);
		llTop = (LinearLayout) findViewById(R.id.llTop);
		llBody = (LinearLayout) findViewById(R.id.llBody);
		llLeftMenu = (LinearLayout) findViewById(R.id.llLeftMenu);
		ivMenu = (ImageView) findViewById(R.id.ivMenu);
		ivLogo = (ImageView) findViewById(R.id.base_logo);
		dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		llGroup = (LinearLayout) findViewById(R.id.llGroup);

		tv1 = (TextView) findViewById(R.id.tvCalender);
		tv2 = (TextView) findViewById(R.id.tvSuppliers);
		tv3 = (TextView) findViewById(R.id.tvMaterials);
		tv4 = (TextView) findViewById(R.id.tvMelForms);
		tv5 = (TextView) findViewById(R.id.tvOthers);
		tv6 = (TextView) findViewById(R.id.tvMore);
	}

	private void setBaseClickListeners() {
		llLeftMenu.setOnClickListener(this);
		tv1.setOnClickListener(this);
		tv2.setOnClickListener(this);
		tv3.setOnClickListener(this);
		tv4.setOnClickListener(this);
		tv5.setOnClickListener(this);
		tv6.setOnClickListener(this);

		ivLogo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(BaseActivity.this, HomeActivity.class));
				dLayout.closeDrawers();
				finish();
			}
		});
	}

	@Override
	public void onClick(View clickedView) {

		switch (clickedView.getId()) {

		case R.id.llLeftMenu:
			if (enableDrawer) {
				if (dLayout.isDrawerOpen(Gravity.LEFT)) {
					dLayout.closeDrawer(Gravity.LEFT);
				} else {
					dLayout.openDrawer(Gravity.LEFT);
				}
			}
			break;

		case R.id.tvCalender:
			// startActivity(new Intent(BaseActivity.this,
			// SimpleCalendarViewActivity.class));
			// new GetSupervisorAllJobsTask(BaseActivity.this).execute();
			dLayout.closeDrawers();
			break;

		case R.id.tvSuppliers:
			startActivity(new Intent(BaseActivity.this, SuppliersActivity.class));
			dLayout.closeDrawers();
			break;

		case R.id.tvMaterials:
			startActivity(new Intent(BaseActivity.this, MaterialsActivity.class));
			dLayout.closeDrawers();
			break;

		case R.id.tvMelForms:
			startActivity(new Intent(BaseActivity.this, MelFormsActivity.class));
			dLayout.closeDrawers();
			break;

		case R.id.tvOthers:
			startActivity(new Intent(BaseActivity.this, OthersActivity.class));
			dLayout.closeDrawers();
			break;

		case R.id.tvMore:
			startActivity(new Intent(BaseActivity.this, MoreActivity.class));
			dLayout.closeDrawers();
			break;

		default:
			break;
		}
	}

	public void enableDrawer(boolean enableDrawer) {
		this.enableDrawer = enableDrawer;
		if (enableDrawer)
			dLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		else
			dLayout.setDrawerLockMode(Gravity.LEFT);
	}

}
