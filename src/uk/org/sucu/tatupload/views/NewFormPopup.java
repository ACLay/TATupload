package uk.org.sucu.tatupload.views;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.org.sucu.tatupload.R;
import uk.org.sucu.tatupload.TatUploadApplication;
import uk.org.sucu.tatupload.message.Parser;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.view.View;
import android.widget.TextView;

public class NewFormPopup extends TextInputPopup {

	private Activity activity;

	public NewFormPopup(Activity a){
		super(a);

		activity = a;

		header.setText(R.string.new_form_name_request);

		Date d = new Date(Calendar.getInstance().getTimeInMillis());
		String date = DateFormat.getDateInstance().format(d);
		textBox.setText(date + " " + context.getString(R.string.text_a_toastie));

		button1.setText(R.string.make_new_form);
		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//get the text
				String formName = textBox.getText().toString();
				//make the url
				Uri uri = Parser.createNewFormUri(formName);
				//TODO make a single sendURIintent method
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
				//open all this apps requests in the same tab, prevents new ones with each call
				browserIntent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
				//allows a new task to be started outside of a current task
				browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(browserIntent);

				//store the formName
				TatUploadApplication.setFormName(formName);
				//show it in the settings menu
				TextView formIdTextView = (TextView) activity.findViewById(R.id.formNameTextView);
				formIdTextView.setText(formName);

				//close the popup
				dismiss();

			}
		});


		button2.setText(R.string.cancel);
		button2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//close the popup
				dismiss();
			}
		});

	}

}
