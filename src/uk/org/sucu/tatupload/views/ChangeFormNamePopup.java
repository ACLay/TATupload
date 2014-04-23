package uk.org.sucu.tatupload.views;

import uk.org.sucu.tatupload.R;
import uk.org.sucu.tatupload.TatUploadApplication;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

@SuppressLint("ViewConstructor")
public class ChangeFormNamePopup extends TextInputPopup {

	private Activity activity;
	
	//need to use Activity to make the findViewById work
	public ChangeFormNamePopup(Activity a) {
		super(a);
		
		activity = a;

		header.setText(R.string.new_form_id);
		button1.setText(R.string.accept);
		button1.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				//get the name from the EditText
				String name = textBox.getText().toString();
				//store it in the application
				((TatUploadApplication) activity.getApplication()).setFormName(name);
				//show it in the settings menu
				TextView formIdTextView = (TextView) activity.findViewById(R.id.formNameTextView);
				formIdTextView.setText(name);
				//close the popup window
				dismiss();
			}
			
		});
		
		button2.setText(R.string.cancel);
		button2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

}
