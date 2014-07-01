package uk.org.sucu.tatupload.activity;

import java.io.IOException;
import java.util.ArrayList;

import uk.org.sucu.tatupload.ParameterArrayAdapter;
import uk.org.sucu.tatupload.R;
import uk.org.sucu.tatupload.parse.Parameters;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class ParameterViewActivity extends Activity {

	private ArrayList<String> parameter;
	private ParameterArrayAdapter adapter;
	private String parameterIdentifier;
	
	private AlertDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_parameter_view);
		
		Intent intent = getIntent();
		parameterIdentifier = intent.getStringExtra(Parameters.PARAMETER);
		
		TextView tv = (TextView) findViewById(R.id.param_name_label);
		tv.setText(Parameters.getParamDescription(parameterIdentifier, this));
		
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
		String text;
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setTitle(Parameters.getParamDescription(parameterIdentifier, this));
			text = "";
		} else {
			text = Parameters.getParamDescription(parameterIdentifier, this);
		}
		
		TextView tv = (TextView) findViewById(R.id.param_name_label);
		tv.setText(text);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.parameter_view, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		
		switch (item.getItemId()){
			case R.id.restore_default:
				restoreDefaultParameter();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
		
	}

	public void restoreDefaultParameter(){
		ArrayList<String> defaults = Parameters.getDefaultList(parameterIdentifier);
		Parameters.loadParameter(parameterIdentifier, defaults);
		saveParameter();
		adapter.notifyDataSetChanged();
	}

	public void openAddDialogue(View v){
		makeAddPopup();
	}
	public void openEditDialogue(View v){
		makeEditPopup();
	}
	public void openRemoveDialogue(View v){
		makeRemovalPopup();
	}

	
	@SuppressLint("DefaultLocale")
	public void addParameter(){
		
		EditText textbox = (EditText) dialog.findViewById(R.id.addParamTextField);
		String param = textbox.getText().toString().toLowerCase();
		
		if(!parameter.contains(param)){
			parameter.add(param);
			adapter.notifyDataSetChanged();
		}
	}

	@SuppressLint("DefaultLocale")
	public void editParameter(){
		Spinner spin = (Spinner) dialog.findViewById(R.id.editSpinner);
		String toChange = (String) spin.getSelectedItem();
		EditText textbox = (EditText) dialog.findViewById(R.id.editParamTextField);
		
		int index = parameter.indexOf(toChange);
		if(index != -1){//indexOf returns -1 if the object isn't present
			parameter.set(index, textbox.getText().toString().toLowerCase());
			adapter.notifyDataSetChanged();
		}
	}
	
	public void removeParameter(){
		Spinner spin = (Spinner) dialog.findViewById(R.id.removeSpinner);
		String toTake = (String) spin.getSelectedItem();
		parameter.remove(toTake);
		adapter.notifyDataSetChanged();
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
	
	private void makeRemovalPopup(){
		//build the dialog box
		View viewToLoad = LayoutInflater.from(this).inflate(R.layout.remove_param_popup, null);		
		dialog = new AlertDialog.Builder(this)
			.setView(viewToLoad)
			.setPositiveButton(R.string.remove_param, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					removeParameter();
				}
			})
			.setNegativeButton(R.string.cancel, null)
			.create();
		//connect the dialogs spinner to the data structure
		Spinner spin = (Spinner) viewToLoad.findViewById(R.id.removeSpinner);
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,parameter);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(adapter);
		//display the dialog
		dialog.show();
	}

	private void makeEditPopup(){
		View viewToLoad = LayoutInflater.from(this).inflate(R.layout.edit_param_popup, null);	
		//build the dialog box
		dialog = new AlertDialog.Builder(this)
			.setView(viewToLoad)
			.setPositiveButton(R.string.change_param, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					editParameter();
				}
			})
			.setNegativeButton(R.string.cancel, null)
			.create();
		//connect its spinner to the data structure
		Spinner spin = (Spinner) viewToLoad.findViewById(R.id.editSpinner);
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,parameter);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(adapter);
		//display the dialog
		dialog.show();
		
	}
	
	private void makeAddPopup(){
		View viewToLoad = LayoutInflater.from(this).inflate(R.layout.add_param_popup, null);	
		
		dialog = new AlertDialog.Builder(this)
			.setView(viewToLoad)
			.setPositiveButton(R.string.add_param, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					addParameter();
				}
			})
			.setNegativeButton(R.string.cancel, null)
			.create();
		dialog.show();
		
	}
}
