package cn.fmsoft.launcher2;

import cn.fmsoft.launcher2.helper.ExpandableAdapterIcon;
import cn.fmsoft.launcher2.helper.IconModel;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListView;

public class HelperMainActivity extends Activity {
	
	private ExpandableAdapterIcon mAdapterIcon;
	private IconModel mModel;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_icon);

		mModel = new IconModel(this);
		mAdapterIcon = new ExpandableAdapterIcon(this, mModel.getIconList());

		ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListView_Icon);
		expandableListView.setAdapter(mAdapterIcon);
		
		mModel.startLoad();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int item_id = item.getItemId();
		switch (item_id) {
		case R.id.item_by_name:
		case R.id.item_by_package:
		case R.id.item_by_layout:
			item.setChecked(true);
			break;
			
		default:
			break;
		}
		return true;
	}
}
