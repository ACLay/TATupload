package uk.org.sucu.tatupload.views;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PropertyView extends LinearLayout{
	
	private String text;
	private TextView tv;
	private EditText edt;
	private Button button;
	
	public PropertyView(String str, Context context) {
		super(context);
		text = str;
		tv = new TextView(context);
		edt = new EditText(context);
		button = new Button(context);
		
		tv.setText(text);
		button.setText("Edit");
	}

	
	
}
