package uk.org.sucu.tatupload;

import java.util.ArrayList;

import uk.org.sucu.tatupload.message.SmsList;
import uk.org.sucu.tatupload.message.Text;
import uk.org.sucu.tatupload.parse.Parameters;
import android.app.Application;

public class TatUploadApplication extends Application {
	
	
	public void onCreate(){
		super.onCreate();
		//Load the data extraction parameters, which need de-serializing to be usable, for improved access time.
		
		ArrayList<String> flavourList = Settings.getSavedParameter(this, Parameters.FLAVOUR_PARAMETER);	
		ArrayList<String> locationList = Settings.getSavedParameter(this, Parameters.LOCATION_PARAMETER);
		ArrayList<String> questionList = Settings.getSavedParameter(this, Parameters.QUESTION_PARAMETER);

		Parameters.loadParameters(flavourList, locationList, questionList);
		
		ArrayList<Text> savedQueue = Settings.getSavedTexts(this);
		SmsList.addTexts(savedQueue);
	}


}
