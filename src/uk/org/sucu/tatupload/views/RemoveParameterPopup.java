package uk.org.sucu.tatupload.views;

import java.util.ArrayList;
import java.util.Set;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;

public class RemoveParameterPopup extends PopupWindow {

	private Spinner dropDown;
	private Set<String> parameterStrings;
	
	public RemoveParameterPopup(Context c, Set<String> strings){
		super(c);
		
		parameterStrings = strings;
		
		LinearLayout layout = new LinearLayout(c);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		dropDown = new Spinner(c);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(c,android.R.layout.simple_spinner_dropdown_item, makeList(strings));
		dropDown.setAdapter(adapter);
		
		Button removeButton = new Button(c);
		removeButton.setText("Remove");
		removeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String toRemove = (String) dropDown.getSelectedItem();
				parameterStrings.remove(toRemove);
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
		
		LinearLayout buttonLayout = new LinearLayout(c);
		buttonLayout.addView(removeButton);
		buttonLayout.addView(cancelButton);
		
		layout.addView(dropDown);
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
