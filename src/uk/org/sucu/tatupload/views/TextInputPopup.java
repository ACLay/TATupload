package uk.org.sucu.tatupload.views;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class TextInputPopup extends PopupWindow {

	protected TextView header;
	protected EditText textBox;
	protected Button button1;
	protected Button button2;
	
	protected Context context;
	
	public TextInputPopup(Context c){
		super(c);
		
		context = c;
		
		LinearLayout layout = new LinearLayout(c);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		header = new TextView(c);
		
		textBox = new EditText(c);
		
		LinearLayout buttonLayout = new LinearLayout(c);
		
		button1 = new Button(c);
		
		button2 = new Button(c);
		
		buttonLayout.addView(button1);
		buttonLayout.addView(button2);
		
		layout.addView(header);
		layout.addView(textBox);
		layout.addView(buttonLayout);
		
		this.setContentView(layout);
	}
	
	
	
}
