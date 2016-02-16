package uk.org.sucu.tatupload;

import java.util.ArrayList;

import uk.org.sucu.tatupload.message.SmsList;
import uk.org.sucu.tatupload.message.Text;
import uk.org.sucu.tatupload.network.AuthManager;
import uk.org.sucu.tatupload.parse.Parameters;
import android.app.Application;

public class TatUploadApplication extends Application {
	
	
	public void onCreate(){
		super.onCreate();
		//Load the data extraction parameters, which need de-serializing to be usable, for improved access time.
		
		Settings settings = new Settings(this);
		
		ArrayList<String> flavourList = settings.getSavedParameter(Parameters.FLAVOUR_PARAMETER);	
		ArrayList<String> locationList = settings.getSavedParameter(Parameters.LOCATION_PARAMETER);
		ArrayList<String> questionList = settings.getSavedParameter(Parameters.QUESTION_PARAMETER);

		Parameters.loadParameters(flavourList, locationList, questionList);
		
		ArrayList<Text> pendingTexts = settings.loadPendingTexts();
		SmsList.getPendingList().addTexts(pendingTexts);
		
		ArrayList<Text> uploadedTexts = settings.loadUploadedTexts();
		SmsList.getUploadedList().addTexts(uploadedTexts);

		AuthManager.initializeCredential(this);

		Notifications.updateNotification(this);
	}


}
