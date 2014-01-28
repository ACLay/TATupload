package uk.org.sucu.tatupload.views;

import uk.org.sucu.tatupload.MainActivity;
import uk.org.sucu.tatupload.R;
import uk.org.sucu.tatupload.TatUploadApplication;
import android.view.View;
import android.widget.TextView;

public class ChangeFormIdPopup extends TextInputPopup {

	private final MainActivity activity;
	
	//need to use Activity to make the findViewById work
	public ChangeFormIdPopup(MainActivity a) {
		super(a);
		
		activity = a;
		// TODO Auto-generated constructor stub
		header.setText(R.string.new_form_id);
		button1.setText(R.string.accept);
		button1.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				String id = textBox.getText().toString();
				TatUploadApplication.setFormID(id);
				TextView formIdTextView = (TextView) activity.findViewById(R.id.formIdTextView);
				String txt = context.getString(R.string.form_id) + id;
				formIdTextView.setText(txt);
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
