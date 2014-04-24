package uk.org.sucu.tatupload;

import java.util.ArrayList;

import uk.org.sucu.tatupload.parse.Parameters;
import uk.org.sucu.tatupload.views.AddParameterPopup;
import uk.org.sucu.tatupload.views.EditParameterPopup;
import uk.org.sucu.tatupload.views.RemoveParameterPopup;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;

public class ParameterViewActivity extends Activity {

	private ArrayList<String> parameter;
	private ParameterArrayAdapter adapter;
	private String parameterIdentifier;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_parameter_view);
		
		Intent intent = getIntent();
		parameterIdentifier = intent.getStringExtra(Parameters.PARAMETER);
		
		parameter = Parameters.getList(parameterIdentifier);
		if(parameterIdentifier == null) {
			parameterIdentifier = null;
			parameter = new ArrayList<String>();
			parameter.add("This shouldn't appear");
		}
		
		
		ListView paramListView = (ListView) findViewById(R.id.param_list);
		adapter = new ParameterArrayAdapter(this, R.id.param_list, parameter);
		paramListView.setAdapter(adapter);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.parameter_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void openAddDialogue(View v){
		AddParameterPopup popup = new AddParameterPopup(this, parameter);
		popup.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				adapter.notifyDataSetChanged();
				saveParameter();
			}
		});
		popup.showAtLocation(findViewById(R.id.parameterLayout), Gravity.CENTER, 0, 0);
	}
	public void openEditDialogue(View v){
		EditParameterPopup popup = new EditParameterPopup(this, parameter);
		popup.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				adapter.notifyDataSetChanged();
				saveParameter();
			}
		});
		popup.showAtLocation(findViewById(R.id.parameterLayout), Gravity.CENTER, 0, 0);
	}
	public void openRemoveDialogue(View v){
		RemoveParameterPopup popup = new RemoveParameterPopup(this, parameter);
		popup.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				adapter.notifyDataSetChanged();
				saveParameter();
			}
		});
		popup.showAtLocation(findViewById(R.id.parameterLayout), Gravity.CENTER, 0, 0);
	}
	
	private void saveParameter(){
		if(parameterIdentifier != null){
			Parameters.saveParameter(parameterIdentifier, this);
		}
	}
	
}
