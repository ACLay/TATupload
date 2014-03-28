package uk.org.sucu.tatupload.views;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class AddParameterPopup extends PopupWindow {

	private ArrayList<String> parameterStrings;
	private EditText textbox;
	
	public AddParameterPopup(Context context, ArrayList<String> params) {
		// TODO Auto-generated constructor stub
		super();
		
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		textbox = new EditText(context);
		
		Button addButton = new Button(context);
		addButton.setText("Add");
		addButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parameterStrings.add(textbox.getText().toString().toLowerCase());
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
		
		LinearLayout buttonLayout = new LinearLayout(context);
		buttonLayout.addView(addButton);
		buttonLayout.addView(cancelButton);
		
		layout.addView(textbox);
		layout.addView(buttonLayout);
		
		this.setContentView(layout);
		
	}

	

}
