package uk.org.sucu.tatupload2.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import uk.org.sucu.tatupload2.ParameterArrayAdapter;
import uk.org.sucu.tatupload2.R;
import uk.org.sucu.tatupload2.Settings;
import uk.org.sucu.tatupload2.parse.Parameters;

public class ParameterViewActivity extends AppCompatActivity {

	private ArrayList<String> parameter;
	private ParameterArrayAdapter adapter;
	private String parameterIdentifier;
	
	private AlertDialog dialog;
	private Settings settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_parameter_view);
		
		settings = new Settings(this);
		
		Intent intent = getIntent();
		parameterIdentifier = intent.getStringExtra(Parameters.PARAMETER);
		
		TextView tv = (TextView) findViewById(R.id.param_explanation_label);
		tv.setText(Parameters.getParamExplanation(parameterIdentifier, this));
		
		parameter = Parameters.getList(parameterIdentifier);
		if(parameterIdentifier == null) {
			parameterIdentifier = null;
			parameter = new ArrayList<String>();
			parameter.add(getString(R.string.invalid_item));
		}
		
		
		ListView paramListView = (ListView) findViewById(R.id.param_list);
		adapter = new ParameterArrayAdapter(this, R.id.param_list, parameter);
		paramListView.setAdapter(adapter);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
	}

	private void setupActionBar() {
		ActionBar actionbar = getSupportActionBar();
		if(actionbar != null){
			actionbar.setDisplayHomeAsUpEnabled(true);
			actionbar.setTitle(Parameters.getParamHeading(parameterIdentifier, this));
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.parameter_view, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		int itemId = item.getItemId();
		if(itemId == R.id.restore_default){
			restoreDefaultParameter();
			return true;
		} else if (itemId == android.R.id.home) {
			onBackPressed();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
		
	}

	public void restoreDefaultParameter(){
		ArrayList<String> defaults = Parameters.getDefaultList(parameterIdentifier, this);
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
			saveParameter();
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
			saveParameter();
			adapter.notifyDataSetChanged();
		}
	}
	
	public void removeParameter(){
		Spinner spin = (Spinner) dialog.findViewById(R.id.removeSpinner);
		String toTake = (String) spin.getSelectedItem();
		parameter.remove(toTake);
		saveParameter();
		adapter.notifyDataSetChanged();
	}
	
	private void saveParameter(){
		if(parameterIdentifier != null){
			try {
				settings.saveParameter(parameterIdentifier);
			} catch (IOException e) {
				Toast.makeText(this, R.string.param_save_error, Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@SuppressLint("InflateParams")
	private void makeRemovalPopup(){
		//build the dialog box
		View viewToLoad = LayoutInflater.from(this).inflate(R.layout.remove_param_popup, null);		
		dialog = new AlertDialog.Builder(this)
			.setTitle(R.string.remove_param)
			.setView(viewToLoad)
			.setPositiveButton(R.string.remove_param, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					removeParameter();
				}
			})
			.setNegativeButton(android.R.string.cancel, null)
			.create();
		//connect the dialogs spinner to the data structure
		Spinner spin = (Spinner) viewToLoad.findViewById(R.id.removeSpinner);
		ParameterArrayAdapter adapter = new ParameterArrayAdapter(this, android.R.layout.simple_spinner_item,parameter);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(adapter);
		//display the dialog
		dialog.show();
	}

	@SuppressLint("InflateParams")
	private void makeEditPopup(){
		View viewToLoad = LayoutInflater.from(this).inflate(R.layout.edit_param_popup, null);	
		//build the dialog box
		dialog = new AlertDialog.Builder(this)
			.setTitle(R.string.change_param)
			.setView(viewToLoad)
			.setPositiveButton(R.string.change_param, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					editParameter();
				}
			})
			.setNegativeButton(android.R.string.cancel, null)
			.create();
		//connect its spinner to the data structure
		Spinner spin = (Spinner) viewToLoad.findViewById(R.id.editSpinner);
		ParameterArrayAdapter adapter = new ParameterArrayAdapter(this, android.R.layout.simple_spinner_item,parameter);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(adapter);
		//display the dialog
		dialog.show();
		
	}
	
	@SuppressLint("InflateParams")
	private void makeAddPopup(){
		View viewToLoad = LayoutInflater.from(this).inflate(R.layout.add_param_popup, null);	
		
		dialog = new AlertDialog.Builder(this)
			.setTitle(R.string.add_param)
			.setView(viewToLoad)
			.setPositiveButton(R.string.add_param, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					addParameter();
				}
			})
			.setNegativeButton(android.R.string.cancel, null)
			.create();
		dialog.show();
		
	}
}
