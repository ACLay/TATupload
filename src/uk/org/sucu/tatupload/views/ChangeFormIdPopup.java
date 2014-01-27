package uk.org.sucu.tatupload.views;

import uk.org.sucu.tatupload.R;
import uk.org.sucu.tatupload.TatUploadApplication;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class ChangeFormIdPopup extends TextInputPopup {

	public ChangeFormIdPopup(Context c) {
		super(c);
		// TODO Auto-generated constructor stub
		header.setText(R.string.new_form_id);
		button1.setText(R.string.accept);
		button1.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				String id = textBox.getText().toString();
				TatUploadApplication.setFormID(id);
				TextView formIdTextView = (TextView) v.findViewById(R.id.formIdTextView);
				formIdTextView.setText(R.string.form_id + id);
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
