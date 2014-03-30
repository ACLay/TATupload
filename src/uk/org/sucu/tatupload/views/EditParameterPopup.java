package uk.org.sucu.tatupload.views;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
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
	private ArrayList<String> parameterStrings;
	private EditText textbox;
	
	public EditParameterPopup(Context context, ArrayList<String> strings){
		super(context);
		this.setHeight(300);
		this.setWidth(200);
		
		parameterStrings = strings;
		
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		dropDown = new Spinner(context);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item, parameterStrings);
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
		
		TextView label = new TextView(context);
		label.setText("Change to:");
		
		textbox = new EditText(context);
		
		Button updateButton = new Button(context);
		updateButton.setText("Update");
		updateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String toChange = (String) dropDown.getSelectedItem();
				int index = parameterStrings.indexOf(toChange);
				if(index != -1){//indexOf returns -1 if the object isn't present
					parameterStrings.set(index, textbox.getText().toString().toLowerCase());
				}
				dismiss();
			}
		});
		
		Button cancelButton = new Button(context);
		cancelButton.setText("Cancel");
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		LinearLayout replaceLayout = new LinearLayout(context);
		replaceLayout.addView(label);
		replaceLayout.addView(textbox);
		
		LinearLayout buttonLayout = new LinearLayout(context);
		buttonLayout.addView(updateButton);
		buttonLayout.addView(cancelButton);
		
		layout.addView(dropDown);
		layout.addView(replaceLayout);
		layout.addView(buttonLayout);
		layout.setBackgroundColor(Color.WHITE);
		
		this.setFocusable(true);
		this.setContentView(layout);
		
	}
}
