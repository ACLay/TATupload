package uk.org.sucu.tatupload;

import java.io.IOException;
import java.util.ArrayList;

import uk.org.sucu.tatupload.parse.Parameters;
import uk.org.sucu.tatupload.views.AddParameterPopup;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Spinner;

public class ParameterViewActivity extends Activity {

	private ArrayList<String> parameter;
	private ParameterArrayAdapter adapter;
	private String parameterIdentifier;
	
	private PopupWindow popup;
	
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
		popup = new AddParameterPopup(this, parameter);
		openPopup();
	}
	public void openEditDialogue(View v){
		popup = makeEditPopup();
		openPopup();
	}
	public void openRemoveDialogue(View v){
		popup = makeRemovalPopup();
		openPopup();
	}
	
	private void openPopup(){
		popup.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				adapter.notifyDataSetChanged();
				saveParameter();
			}
		});
		popup.showAtLocation(findViewById(R.id.parameterLayout), Gravity.CENTER, 0, 0);
	}
	
	public void closePopup(){
		popup.dismiss();
	}
	
	public void editParameter(View v){
		Spinner spin = (Spinner) popup.getContentView().findViewById(R.id.removeSpinner);
		String toChange = (String) spin.getSelectedItem();
		EditText textbox = (EditText) popup.getContentView().findViewById(R.id.editParamTextField);
		
		int index = parameter.indexOf(toChange);
		if(index != -1){//indexOf returns -1 if the object isn't present
			parameter.set(index, textbox.getText().toString().toLowerCase());
		}
		closePopup();
	}
	
	public void removeParameter(){
		Spinner spin = (Spinner) popup.getContentView().findViewById(R.id.removeSpinner);
		String toTake = (String) spin.getSelectedItem();
		parameter.remove(toTake);
		closePopup();
	}
	
	private void saveParameter(){
		if(parameterIdentifier != null){
			try {
				Parameters.saveParameter(parameterIdentifier, this);
			} catch (IOException e) {
				new AlertDialog.Builder(this)
						.setTitle("TATupload")  
						.setMessage("Unable to save the parameter.")
						.setPositiveButton("Okay", null)  
						.setCancelable(false)  
						.create()  
						.show();
			}
		}
	}
	
	private PopupWindow makeRemovalPopup(){
		
		View viewToLoad = LayoutInflater.from(this).inflate(R.layout.remove_param_popup, null);		
		PopupWindow popup = new PopupWindow(viewToLoad, 200, 150, true);
		Spinner spin = (Spinner) viewToLoad.findViewById(R.id.removeSpinner);
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,parameter);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(adapter);
		
		Button cancel = (Button) viewToLoad.findViewById(R.id.cancelRemove);
		cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				closePopup();
			}
		});
		
		Button remove = (Button) viewToLoad.findViewById(R.id.removeButton);
		remove.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				removeParameter();
			}
		});
		
		return popup;
	}

	private PopupWindow makeEditPopup(){
		View viewToLoad = LayoutInflater.from(this).inflate(R.layout.edit_param_popup, null);		
		PopupWindow popup = new PopupWindow(viewToLoad, 200, 300, true);
		Spinner spin = (Spinner) viewToLoad.findViewById(R.id.editSpinner);
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,parameter);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(adapter);
		
		Button cancel = (Button) viewToLoad.findViewById(R.id.cancelEdit);
		cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				closePopup();
			}
		});
		
		Button remove = (Button) viewToLoad.findViewById(R.id.editButton);
		remove.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				removeParameter();
			}
		});
		
		return popup;
	}
}
