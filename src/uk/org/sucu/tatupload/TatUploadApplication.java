package uk.org.sucu.tatupload;

import java.util.ArrayList;

import android.app.Application;
import android.telephony.SmsMessage;

public class TatUploadApplication extends Application {
	
	private static boolean processingTexts = true;
	private static boolean confirmSplit = true;
	private static ArrayList<SmsMessage> messages = new ArrayList<SmsMessage>();
	private static String formID;
	private static final String makeFormScript = "https://script.google.com/macros/s/AKfycbxAr_Ji_fzvgYuDCL-Qc1mSvgvxMLM1P38QsFaxHtzKstlEwJmI/exec";
	private static final String uploadScript = "https://script.google.com/macros/s/AKfycbzfPd5U7tbyOmK8EERxB8LPn53CzLy_nzXzAu2jb2_fYC8V_aof/exec";
	
	public static boolean getProcessingTexts(){
		return processingTexts;
	}
	public static void setProcessingTexts(boolean b){
		processingTexts = b;
	}
	
	public static boolean getConfirmSplit(){
		return confirmSplit;
	}
	public static void setConfirmSplit(boolean b){
		confirmSplit = b;
	}
	
	public static ArrayList<SmsMessage> getMessageList(){
		return messages;
	}
	
	public static void setFormID(String newID){
		formID = newID;
	}
	public static String getFormID(){
		return formID;
	}
	
	public static String getMakeFormScriptURL(){
		return makeFormScript;
	}
	
	public static String getUploadScriptURL(){
		return uploadScript;
	}
	
	
	
	
	
	
}
