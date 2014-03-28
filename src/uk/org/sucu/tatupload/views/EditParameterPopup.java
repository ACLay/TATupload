package uk.org.sucu.tatupload.views;

import java.util.ArrayList;
import java.util.Set;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

public class EditParameterPopup extends PopupWindow {
	
	private Spinner dropDown;
	private Set<String> parameterStrings;
	private EditText textbox;
	
	public EditParameterPopup(Context c, Set<String> strings){
		super(c);
		
		parameterStrings = strings;
		
		LinearLayout layout = new LinearLayout(c);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		dropDown = new Spinner(c);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(c,android.R.layout.simple_spinner_dropdown_item, makeList(strings));
		dropDown.setAdapter(adapter);
		dropDown.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				textbox.setText((String)dropDown.getSelectedItem());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		TextView label = new TextView(c);
		label.setText("Change to:");
		
		textbox = new EditText(c);
		
		Button updateButton = new Button(c);
		updateButton.setText("Update");
		updateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String toChange = (String) dropDown.getSelectedItem();
				if(parameterStrings.remove(toChange)){
					parameterStrings.add(textbox.getText().toString().toLowerCase());
				}
				dismiss();
			}
		});
		
		Button cancelButton = new Button(c);
		cancelButton.setText("Cancel");
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		LinearLayout replaceLayout = new LinearLayout(c);
		replaceLayout.addView(label);
		replaceLayout.addView(textbox);
		
		LinearLayout buttonLayout = new LinearLayout(c);
		buttonLayout.addView(updateButton);
		buttonLayout.addView(cancelButton);
		
		layout.addView(dropDown);
		layout.addView(replaceLayout);
		layout.addView(buttonLayout);
		
	}
	
	private ArrayList<String> makeList(Set<String> params){
		ArrayList<String> list = new ArrayList<String>();
		
		for(String s : params){
			list.add(s);
		}
		
		return list;
	}
}
